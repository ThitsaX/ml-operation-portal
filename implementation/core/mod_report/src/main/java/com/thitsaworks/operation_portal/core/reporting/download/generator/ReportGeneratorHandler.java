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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportGeneratorHandler implements ReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorHandler.class);

    private static final int MAX_PENDING_CLAIM_RETRIES = 10;

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;

    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    private final FileStorage fileStorage;

    private final Map<ReportType, ReportTypeGenerator> reportTypeGenerators;

    private final TransactionTemplate requiresNewTransactionTemplate;

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
        this.requiresNewTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        for (ReportTypeGenerator reportTypeGenerator : reportTypeGeneratorList) {
            ReportType reportType = reportTypeGenerator.reportType();
            ReportTypeGenerator previous = this.reportTypeGenerators.put(reportType, reportTypeGenerator);
            if (previous != null) {
                throw new IllegalStateException("Duplicate generator mapping for report type: " + reportType);
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

        try {

            ReportGeneratedFile generatedFile = this.generateReportFile(request);
            String fileUrl = this.uploadReportFile(request, generatedFile);

            this.markReady(request.getId(), fileUrl);

            LOG.info(
                "Report request [{}] completed and saved to [{}]",
                request.getId()
                       .getEntityId(),
                fileUrl);

        } catch (Exception exception) {

            this.markFailed(request.getId(), this.trimError(exception.getMessage()));

            LOG.error("Report request [{}] failed",
                      request.getId()
                             .getEntityId(),
                      exception);
        }

        return true;
    }

    @Override
    @Transactional(
        readOnly = true,
        transactionManager = PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    public long countRunning() {

        return this.reportDownloadRequestRepository.countByStatus(FileDownloadStatus.RUNNING);
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

    private String uploadReportFile(ReportDownloadRequest request, ReportGeneratedFile generatedFile) {

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

        Optional<ReportDownloadRequest> claimed = this.requiresNewTransactionTemplate.execute(
            transactionStatus -> {
                int attempts = 0;

                while (attempts < MAX_PENDING_CLAIM_RETRIES) {

                    Optional<ReportDownloadRequest>
                        nextPending =
                        this.reportDownloadRequestRepository.findTopByStatusOrderByCreatedAtAsc(
                            FileDownloadStatus.PENDING);
                    if (nextPending.isEmpty()) {
                        return Optional.empty();
                    }

                    ReportDownloadRequest request = nextPending.get();
                    Instant now = Instant.now();
                    int updated = this.reportDownloadRequestRepository.updateStatusIfCurrent(
                        request.getId(),
                        FileDownloadStatus.PENDING,
                        FileDownloadStatus.RUNNING,
                        now);

                    if (updated == 1) {
                        request.status(FileDownloadStatus.RUNNING);
                        request.updatedDate(now);
                        return Optional.of(request);
                    }

                    attempts++;
                }

                LOG.warn("Could not claim pending report request after [{}] retries", MAX_PENDING_CLAIM_RETRIES);
                return Optional.empty();
            });

        return claimed == null ? Optional.empty() : claimed;
    }

    private void markReady(ReportDownloadRequestId requestId, String fileUrl) {

        this.requiresNewTransactionTemplate.executeWithoutResult(transactionStatus -> {
            Optional<ReportDownloadRequest> optRequest = this.reportDownloadRequestRepository.findById(
                requestId);
            if (optRequest.isEmpty()) {
                return;
            }

            ReportDownloadRequest request = optRequest.get();
            request.fileUrl(fileUrl);
            request.status(FileDownloadStatus.READY);
            request.errorMessage(null);
            request.updatedDate(Instant.now());
            request.finishedDate(Instant.now());
            this.reportDownloadRequestRepository.save(request);
        });
    }

    private void markFailed(ReportDownloadRequestId requestId, String errorMessage) {

        this.requiresNewTransactionTemplate.executeWithoutResult(transactionStatus -> {
            Optional<ReportDownloadRequest> optRequest = this.reportDownloadRequestRepository.findById(
                requestId);
            if (optRequest.isEmpty()) {
                return;
            }

            ReportDownloadRequest request = optRequest.get();
            request.status(FileDownloadStatus.FAILED);
            request.errorMessage(errorMessage);
            request.updatedDate(Instant.now());
            request.finishedDate(Instant.now());
            this.reportDownloadRequestRepository.save(request);
        });
    }

    public String createFilePath(ReportDownloadRequest request, String extension) {

        String
            normalizedType =
            request.getReportType()
                   .name()
                   .toLowerCase(Locale.ROOT);
        String timestamp = DateTimeFormatter
                               .ofPattern("yyyyMMddHHmmss")
                               .format(LocalDateTime.now(ZoneOffset.UTC));
        String normalizedExt = extension.toLowerCase(Locale.ROOT);

        return normalizedType + "/" + request.getId()
                                             .getEntityId() + "_" + timestamp + "." +
                   normalizedExt;
    }

}
