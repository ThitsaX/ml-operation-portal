package com.thitsaworks.operation_portal.core.reporting.download.generator;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.component.misc.storage.FileStorage;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequestParam;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestParamRepository;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestRepository;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class ReportGeneratorHandler implements ReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorHandler.class);

    private static final int MAX_PENDING_CLAIM_RETRIES = 10;

    private static final int MAX_TRANSACTION_RETRIES = 5;

    private static final Duration RUNNING_STALE_TTL = Duration.ofMinutes(20);

    private static final Duration RUNNING_HEARTBEAT_INTERVAL = Duration.ofMinutes(1);

    private static final String STALE_RUNNING_RECOVERY_MESSAGE = "Auto-recovered stale RUNNING request after restart/error";

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;

    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    private final FileStorage fileStorage;

    private final Map<ReportType, ReportTypeGenerator> reportTypeGenerators;

    private final TransactionTemplate requiresNewTransactionTemplate;

    private final ScheduledExecutorService runningHeartbeatExecutor;

    public ReportGeneratorHandler(ReportDownloadRequestRepository reportDownloadRequestRepository,
                                  ReportDownloadRequestParamRepository reportDownloadRequestParamRepository,
                                  FileStorage fileStorage,
                                  List<ReportTypeGenerator> reportTypeGeneratorList,
                                  @Qualifier(PersistenceQualifiers.Core.TRANSACTION_MANAGER)
                                  PlatformTransactionManager transactionManager) {

        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
        this.reportDownloadRequestParamRepository = reportDownloadRequestParamRepository;
        this.fileStorage = fileStorage;
        this.reportTypeGenerators = new HashMap<>();
        this.requiresNewTransactionTemplate = new TransactionTemplate(transactionManager);
        this.requiresNewTransactionTemplate.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.runningHeartbeatExecutor = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactory() {

                @Override
                public Thread newThread(Runnable runnable) {

                    Thread thread = new Thread(runnable, "report-running-heartbeat");
                    thread.setDaemon(true);
                    return thread;
                }
            });

        for (ReportTypeGenerator reportTypeGenerator : reportTypeGeneratorList) {
            ReportType reportType = reportTypeGenerator.reportType();
            ReportTypeGenerator previous = this.reportTypeGenerators.put(
                reportType, reportTypeGenerator);
            if (previous != null) {
                throw new IllegalStateException(
                    "Duplicate generator mapping for report type: " + reportType);
            }
        }
    }

    @Override
    public boolean generateNextPending() {

        Optional<ReportDownloadRequest> claimedPending = this.claimNextPendingRequest();
        if (claimedPending.isEmpty()) {

            return false;
        }

        ReportDownloadRequest request = claimedPending.get();
        ScheduledFuture<?> heartbeatFuture = this.startRunningHeartbeat(request.getId());

        try {

            ReportGeneratedFile generatedFile = this.generateReportFile(request);
            String fileUrl = this.uploadReportFile(request, generatedFile);

            boolean markedReady = this.markReady(request.getId(), fileUrl);
            if (markedReady) {
                LOG.info(
                    "Report request [{}] completed and saved to [{}]",
                    request.getId().getEntityId(), fileUrl);
            } else {
                LOG.warn(
                    "Report request [{}] generated file [{}] but status was no longer RUNNING during READY transition",
                    request.getId().getEntityId(), fileUrl);
            }

        } catch (Exception exception) {

            try {
                boolean markedFailed = this.markFailed(
                    request.getId(), this.trimError(exception.getMessage()));
                if (!markedFailed) {
                    LOG.warn(
                        "Report request [{}] failed but status was no longer RUNNING during FAILED transition",
                        request.getId().getEntityId());
                }
            } catch (Exception markFailedException) {
                LOG.error(
                    "Could not persist FAILED status for report request [{}]",
                    request.getId().getEntityId(), markFailedException);
            }

            LOG.error("Report request [{}] failed", request.getId().getEntityId(), exception);
        } finally {
            heartbeatFuture.cancel(false);
        }

        return true;
    }

    @Override
    @Transactional(
        readOnly = true,
        transactionManager = PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    public long countRunning() {

        Instant freshThreshold = Instant.now().minus(RUNNING_STALE_TTL);
        return this.reportDownloadRequestRepository.countByStatusAndUpdatedAtGreaterThanEqual(
            FileDownloadStatus.RUNNING, freshThreshold);
    }

    @PostConstruct
    public void recoverStaleRunningOnStartup() {

        this.recoverStaleRunningRequests();
    }

    @Scheduled(
        fixedDelay = 300000L,
        initialDelay = 300000L)
    public void recoverStaleRunningRequests() {

        this.executeWithRetry(
            "recover stale running requests", () -> {
                this.requiresNewTransactionTemplate.executeWithoutResult(transactionStatus -> {
                    Instant now = Instant.now();
                    Instant staleBefore = now.minus(RUNNING_STALE_TTL);
                    int recovered = this.reportDownloadRequestRepository.failStaleRunningRequests(
                        FileDownloadStatus.RUNNING, FileDownloadStatus.FAILED,
                        STALE_RUNNING_RECOVERY_MESSAGE, staleBefore, now, now);

                    if (recovered > 0) {
                        LOG.warn(
                            "Recovered [{}] stale report requests stuck in RUNNING state",
                            recovered);
                    }
                });
            });
    }

    @PreDestroy
    public void shutdownRunningHeartbeatExecutor() {

        this.runningHeartbeatExecutor.shutdownNow();
    }

    private ReportGeneratedFile generateReportFile(ReportDownloadRequest request)
        throws ReportException, IOException {

        Map<String, String> params = this.paramsByRequestId(request.getId());
        ReportType reportType = request.getReportType();
        ReportTypeGenerator reportTypeGenerator = this.reportTypeGenerators.get(reportType);

        if (reportTypeGenerator == null) {
            throw new IllegalArgumentException("Unsupported report type: " + reportType);
        }

        return reportTypeGenerator.generate(request, params);
    }

    private String uploadReportFile(ReportDownloadRequest request,
                                    ReportGeneratedFile generatedFile) {

        var fileLocation = this.createFilePath(request, generatedFile.extension());

        return this.fileStorage.upload(
            fileLocation, generatedFile.bytes(), generatedFile.extension());
    }

    private Map<String, String> paramsByRequestId(ReportDownloadRequestId requestId) {

        List<ReportDownloadRequestParam> params = this.reportDownloadRequestParamRepository.findByRequestId(
            requestId);
        Map<String, String> paramMap = new HashMap<>();

        for (ReportDownloadRequestParam param : params) {

            paramMap.put(param.getParamKey(), param.getParamValue());
        }

        return paramMap;
    }

    private String trimError(String message) {

        if (message == null) {

            return "Unexpected error";
        }

        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }

    private Optional<ReportDownloadRequest> claimNextPendingRequest() {

        Optional<ReportDownloadRequest> claimed = this.executeWithRetry(
            "claim next pending report",
            () -> this.requiresNewTransactionTemplate.execute(transactionStatus -> {
                int attempts = 0;

                while (attempts < MAX_PENDING_CLAIM_RETRIES) {

                    Optional<ReportDownloadRequest> nextPending = this.reportDownloadRequestRepository.findTopByStatusOrderByCreatedAtAsc(
                        FileDownloadStatus.PENDING);
                    if (nextPending.isEmpty()) {
                        return Optional.empty();
                    }

                    ReportDownloadRequest request = nextPending.get();
                    Instant now = Instant.now();
                    int updated = this.reportDownloadRequestRepository.updateStatusIfCurrent(
                        request.getId(), FileDownloadStatus.PENDING, FileDownloadStatus.RUNNING,
                        now);

                    if (updated == 1) {
                        request.status(FileDownloadStatus.RUNNING);
                        request.updatedDate(now);
                        return Optional.of(request);
                    }

                    attempts++;
                }

                LOG.warn(
                    "Could not claim pending report request after [{}] retries",
                    MAX_PENDING_CLAIM_RETRIES);
                return Optional.empty();
            }));

        return claimed == null ? Optional.empty() : claimed;
    }

    private boolean markReady(ReportDownloadRequestId requestId, String fileUrl) {

        return Boolean.TRUE.equals(this.executeWithRetry(
            "mark report ready", () -> {
                Instant now = Instant.now();
                Integer updated = this.requiresNewTransactionTemplate.execute(
                    transactionStatus -> this.reportDownloadRequestRepository.transitionToReadyIfRunning(
                        requestId, FileDownloadStatus.RUNNING, FileDownloadStatus.READY, fileUrl,
                        now, now));

                return updated != null && updated == 1;
            }));
    }

    private boolean markFailed(ReportDownloadRequestId requestId, String errorMessage) {

        return Boolean.TRUE.equals(this.executeWithRetry(
            "mark report failed", () -> {
                Instant now = Instant.now();
                Integer updated = this.requiresNewTransactionTemplate.execute(
                    transactionStatus -> this.reportDownloadRequestRepository.transitionToFailedIfRunning(
                        requestId, FileDownloadStatus.RUNNING, FileDownloadStatus.FAILED,
                        errorMessage, now, now));

                return updated != null && updated == 1;
            }));
    }

    private void executeWithRetry(String operationName, Runnable operation) {

        this.executeWithRetry(
            operationName, () -> {
                operation.run();
                return null;
            });
    }

    private <T> T executeWithRetry(String operationName, Supplier<T> operation) {

        RuntimeException lastException = null;

        for (int attempt = 1; attempt <= MAX_TRANSACTION_RETRIES; attempt++) {
            try {
                return operation.get();
            } catch (RuntimeException exception) {
                if (!this.isRetryableLockException(exception) ||
                        attempt == MAX_TRANSACTION_RETRIES) {
                    throw exception;
                }

                lastException = exception;
                long backoffMillis = 200L * attempt;
                LOG.warn(
                    "Retrying operation [{}] after lock/deadlock failure (attempt {}/{})",
                    operationName, attempt, MAX_TRANSACTION_RETRIES, exception);
                this.sleepQuietly(backoffMillis);
            }
        }

        if (lastException != null) {
            throw lastException;
        }

        return null;
    }

    private ScheduledFuture<?> startRunningHeartbeat(ReportDownloadRequestId requestId) {

        long intervalSeconds = RUNNING_HEARTBEAT_INTERVAL.toSeconds();
        return this.runningHeartbeatExecutor.scheduleAtFixedRate(
            () -> this.touchRunningHeartbeat(requestId), intervalSeconds, intervalSeconds,
            TimeUnit.SECONDS);
    }

    private void touchRunningHeartbeat(ReportDownloadRequestId requestId) {

        try {
            boolean touched = this.executeWithRetry(
                "touch running heartbeat", () -> {
                    Integer updated = this.requiresNewTransactionTemplate.execute(
                        transactionStatus -> this.reportDownloadRequestRepository.touchRunningRequest(
                            requestId, FileDownloadStatus.RUNNING, Instant.now()));
                    return updated != null && updated == 1;
                });

            if (!touched) {
                LOG.debug(
                    "Skipped heartbeat for request [{}] because status is no longer RUNNING",
                    requestId.getEntityId());
            }
        } catch (Exception exception) {
            LOG.warn(
                "Could not update heartbeat for running report request [{}]: {}",
                requestId.getEntityId(), exception.getMessage());
        }
    }

    private boolean isRetryableLockException(Throwable throwable) {

        Throwable current = throwable;
        while (current != null) {
            if (current instanceof TransientDataAccessException) {
                return true;
            }

            String message = current.getMessage();
            if (message != null) {
                String normalized = message.toLowerCase(Locale.ROOT);
                if (normalized.contains("deadlock") || normalized.contains("lock wait timeout") ||
                        normalized.contains("could not acquire lock")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }

    private void sleepQuietly(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public String createFilePath(ReportDownloadRequest request, String extension) {

        String normalizedType = request.getReportType().name().toLowerCase(Locale.ROOT);
        String timestamp = DateTimeFormatter
                               .ofPattern("ddMMMyyyy")
                               .format(LocalDateTime.now(ZoneOffset.UTC));
        String normalizedExt = extension.toLowerCase(Locale.ROOT);
        String fileName = this.buildReportFileName(request, timestamp, normalizedExt);

        return normalizedType + "/" + fileName;
    }

    private String buildReportFileName(ReportDownloadRequest request,
                                       String timestamp,
                                       String extension) {

        return switch (request.getReportType()) {
            case SETTLEMENT_DETAIL -> "DFSPSettlementDetailReport-" + timestamp + "." + extension;
            case TransactionDetail -> "TransactionDetailReport-" + timestamp + "." + extension;
            case MANAGEMENT_SUMMARY -> "ManagementSummaryReport-" + timestamp + "." + extension;
            case AUDIT -> "AuditReport-" + timestamp + "." + extension;
            case SETTLEMENT_BANK -> "SettlementBankReport-" + timestamp + "." + extension;
            case SETTLEMENT_BANK_USECASE ->
                "SettlementBankReport_UseCase-" + timestamp + "." + extension;
            case SETTLEMENT_BANK_OVERVIEW ->
                "SettlementBankOverviewReport-" + timestamp + "." + extension;
            case SETTLEMENT_AUDIT -> "SettlementAuditReport-" + timestamp + "." + extension;

            case SETTLEMENT_STATEMENT ->
                "DFSPSettlementStatementReport-" + timestamp + "." + extension;
            case SETTLEMENT_SUMMARY -> "DFSPSettlementReport-" + timestamp + "." + extension;
        };
    }

}
