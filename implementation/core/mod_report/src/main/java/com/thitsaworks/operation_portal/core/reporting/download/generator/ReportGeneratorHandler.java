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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ReportGeneratorHandler implements ReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorHandler.class);

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;

    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    private final FileStorage fileStorage;

    private final GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    private final GenerateTransactionDetailReportCommand generateTransactionDetailReportCommand;

    private final GenerateAuditReportCommand generateAuditReportCommand;

    public ReportGeneratorHandler(ReportDownloadRequestRepository reportDownloadRequestRepository,
                                  ReportDownloadRequestParamRepository reportDownloadRequestParamRepository,
                                  FileStorage fileStorage,
                                  GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand,
                                  GenerateTransactionDetailReportCommand generateTransactionDetailReportCommand,
                                  GenerateAuditReportCommand generateAuditReportCommand) {

        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
        this.reportDownloadRequestParamRepository = reportDownloadRequestParamRepository;
        this.fileStorage = fileStorage;
        this.generateSettlementDetailReportCommand = generateSettlementDetailReportCommand;
        this.generateTransactionDetailReportCommand = generateTransactionDetailReportCommand;
        this.generateAuditReportCommand = generateAuditReportCommand;
    }

    @Override
    @Transactional(transactionManager = PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    public boolean generateNextPending() {

        Optional<ReportDownloadRequest> nextPending = this.reportDownloadRequestRepository.findTopByStatusOrderByCreatedAtAsc(
            FileDownloadStatus.PENDING);

        if (nextPending.isEmpty()) {

            return false;
        }

        ReportDownloadRequest request = nextPending.get();
        request.status(FileDownloadStatus.RUNNING);
        request.updatedDate(Instant.now());
        this.reportDownloadRequestRepository.save(request);

        try {

            GeneratedFile generatedFile = this.generateReportFile(request);
            String fileUrl = this.uploadReportFile(request, generatedFile);

            request.fileUrl(fileUrl);
            request.status(FileDownloadStatus.READY);
            request.errorMessage(null);
            request.updatedDate(Instant.now());
            request.finishedDate(Instant.now());
            this.reportDownloadRequestRepository.save(request);

            LOG.info(
                "Report request [{}] completed and saved to [{}]", request.getId().getEntityId(),
                fileUrl);

        } catch (Exception exception) {

            request.status(FileDownloadStatus.FAILED);
            request.errorMessage(this.trimError(exception.getMessage()));
            request.updatedDate(Instant.now());
            request.finishedDate(Instant.now());
            this.reportDownloadRequestRepository.save(request);

            LOG.error("Report request [{}] failed", request.getId().getEntityId(), exception);
        }

        return true;
    }

    private GeneratedFile generateReportFile(ReportDownloadRequest request)
        throws ReportException, IOException {

        Map<String, String> params = this.paramsByRequestId(request.getId());

        ReportType reportType = request.getReportType();

        if (reportType == ReportType.SETTLEMENT_DETAIL) {
            return this.generateSettlementDetail(request, params);
        }

        if (reportType == ReportType.TRANSACTION_DETAIL) {
            return this.generateTransactionDetail(request, params);
        }

        if (reportType == ReportType.AUDIT) {
            return this.generateAuditReport(request, params);
        }

        throw new IllegalArgumentException("Unsupported report type: " + reportType);
    }

    private GeneratedFile generateSettlementDetail(ReportDownloadRequest request,
                                                   Map<String, String> params)
        throws ReportException {

        String settlementId = this.requireParam(params, "settlementId");
        String fspId = this.requireParam(params, "fspId");
        String dfspName = params.getOrDefault("dfspName", fspId);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.fileType(request.getFileType());

        GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(
            new GenerateSettlementDetailReportCommand.Input(
                settlementId, fspId, dfspName, fileType,
                timezoneOffset));

        return new GeneratedFile(output.settlementDetailRptByte(), fileType);
    }

    private GeneratedFile generateTransactionDetail(ReportDownloadRequest request,
                                                    Map<String, String> params)
        throws ReportException, IOException {

        Instant startDate = Instant.parse(this.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(this.requireParam(params, "endDate"));
        String state = this.normalizeAllToken(params.getOrDefault("state", "All"));
        String dfspId = this.normalizeAllToken(params.getOrDefault("dfspId", "All"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.fileType(request.getFileType());

        int pageSize = this.generateTransactionDetailReportCommand.transactionPageSize();

        int totalRowCount = this.generateTransactionDetailReportCommand.countRows(
            new GenerateTransactionDetailReportCommand.CountInput(
                startDate, endDate, state, dfspId,
                timezoneOffset));

        LOG.info("Transaction Detail Report Total Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {

            GenerateTransactionDetailReportCommand.Output output = this.generateTransactionDetailReportCommand.execute(
                new GenerateTransactionDetailReportCommand.Input(
                    startDate, endDate, state, dfspId,
                    fileType, timezoneOffset, 0, pageSize));

            return new GeneratedFile(output.transactionDetailRptByte(), fileType);
        }

        return this.generateTransactionDetailPagedZip(
            startDate, endDate, state, dfspId,
            timezoneOffset, fileType, totalRowCount, pageSize);
    }

    private GeneratedFile generateTransactionDetailPagedZip(Instant startDate,
                                                            Instant endDate,
                                                            String state,
                                                            String dfspId,
                                                            String timezoneOffset,
                                                            String fileType,
                                                            int totalRowCount,
                                                            int pageSize)
        throws ReportException, IOException {

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipBytes)) {

            int chunkNo = 1;

            for (int offset = 0; offset < totalRowCount; offset += pageSize) {

                int limit = Math.min(pageSize, totalRowCount - offset);

                GenerateTransactionDetailReportCommand.Output chunkOutput = this.generateTransactionDetailReportCommand.execute(
                    new GenerateTransactionDetailReportCommand.Input(
                        startDate, endDate, state, dfspId, fileType, timezoneOffset, offset,
                        limit));

                ZipEntry entry = new ZipEntry(
                    "transaction_detail_part_" + chunkNo + "." + fileType);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(chunkOutput.transactionDetailRptByte());
                zipOutputStream.closeEntry();
                chunkNo++;
            }
        }

        return new GeneratedFile(zipBytes.toByteArray(), "zip");
    }

    private GeneratedFile generateAuditReport(ReportDownloadRequest request,
                                              Map<String, String> params)
        throws ReportException, IOException {

        Instant fromDate = Instant.parse(this.requireParam(params, "fromDate"));
        Instant toDate = Instant.parse(this.requireParam(params, "toDate"));
        String realmId = this.normalizeOptionalFilter(params.get("realmId"));
        String userId = this.normalizeOptionalFilter(params.get("userId"));
        String actionId = this.normalizeOptionalFilter(params.get("actionId"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.fileType(request.getFileType());
        List<String> grantedActionList = this.parseListParam(params.get("grantedActionList"));

        int pageSize = this.generateAuditReportCommand.auditPageSize();
        int totalRowCount = this.generateAuditReportCommand.countRows(
            new GenerateAuditReportCommand.CountInput(
                realmId, fromDate, toDate, userId, actionId, grantedActionList));

        if (totalRowCount <= pageSize) {

            GenerateAuditReportCommand.Output output = this.generateAuditReportCommand.execute(
                new GenerateAuditReportCommand.Input(
                    realmId, fromDate, toDate, timezoneOffset, userId, actionId, fileType,
                    grantedActionList, 0, pageSize));

            return new GeneratedFile(output.auditRptByte(), fileType);
        }

        return this.generateAuditPagedZip(
            realmId, fromDate, toDate, timezoneOffset, userId, actionId, fileType,
            grantedActionList, totalRowCount, pageSize);
    }

    private GeneratedFile generateAuditPagedZip(String realmId,
                                                Instant fromDate,
                                                Instant toDate,
                                                String timezoneOffset,
                                                String userId,
                                                String actionId,
                                                String fileType,
                                                List<String> grantedActionList,
                                                int totalRowCount,
                                                int pageSize)
        throws ReportException, IOException {

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipBytes)) {

            int chunkNo = 1;

            for (int offset = 0; offset < totalRowCount; offset += pageSize) {

                int limit = Math.min(pageSize, totalRowCount - offset);

                GenerateAuditReportCommand.Output chunkOutput = this.generateAuditReportCommand.execute(
                    new GenerateAuditReportCommand.Input(
                        realmId, fromDate, toDate, timezoneOffset, userId, actionId, fileType,
                        grantedActionList, offset, limit));

                ZipEntry entry = new ZipEntry("audit_part_" + chunkNo + "." + fileType);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(chunkOutput.auditRptByte());
                zipOutputStream.closeEntry();
                chunkNo++;
            }
        }

        return new GeneratedFile(zipBytes.toByteArray(), "zip");
    }

    private String uploadReportFile(ReportDownloadRequest request, GeneratedFile generatedFile) {

        var fileLocation = this.createFilePath(request, generatedFile.extension);

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

    private String requireParam(Map<String, String> params, String key) {

        String value = params.get(key);

        if (value == null || value.isBlank()) {

            throw new IllegalArgumentException("Missing required parameter: " + key);
        }

        return value;
    }


    private String fileType(String fileType) throws ReportException {

        String normalized = fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
        return normalized;
    }

    private String trimError(String message) {

        if (message == null) {

            return "Unexpected error";
        }

        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }

    private String normalizeAllToken(String value) {

        if (value == null) {

            return "All";
        }

        return "all".equalsIgnoreCase(value.trim()) ? "All" : value.trim();
    }

    private String normalizeOptionalFilter(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.isEmpty() || "all".equalsIgnoreCase(trimmed)) {
            return null;
        }

        return trimmed;
    }

    private List<String> parseListParam(String value) {

        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                     .map(String::trim)
                     .filter(item -> !item.isEmpty())
                     .toList();
    }

    private record GeneratedFile(byte[] bytes, String extension) {

    }

    public String createFilePath(ReportDownloadRequest request, String extension) {

        String normalizedType = request.getReportType().name().toLowerCase(Locale.ROOT);
        String timestamp = DateTimeFormatter
                               .ofPattern("yyyyMMddHHmmss")
                               .format(LocalDateTime.now(ZoneOffset.UTC));
        String normalizedExt = extension.toLowerCase(Locale.ROOT);

        return normalizedType + "/" + request.getId().getEntityId() + "_" + timestamp + "." +
                   normalizedExt;
    }

}
