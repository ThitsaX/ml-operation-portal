package com.thitsaworks.operation_portal.core.reporting.download.request;

import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.reporting.download.data.ReportDownloadRequestData;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequestParam;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestParamRepository;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestRepository;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportDownloadRequestManager {

    private static final int MAX_RETRY_COUNT = 3;

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;

    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    public ReportDownloadRequestManager(ReportDownloadRequestRepository reportDownloadRequestRepository,
                                        ReportDownloadRequestParamRepository reportDownloadRequestParamRepository) {

        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
        this.reportDownloadRequestParamRepository = reportDownloadRequestParamRepository;
    }

    public Optional<ReportDownloadRequest> findExistingRequest(ReportType reportType,
                                                               String fileType,
                                                               LocalDate dataVersion,
                                                               Map<String, String> params) {

        String normalizedFileType = normalizeFileType(fileType);
        LocalDate normalizedDataVersion = dataVersion == null ? LocalDate.now(ZoneOffset.UTC) : dataVersion;
        String signature = ReportRequestSignature.from(reportType.name(), normalizedFileType, params);

        return this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
            reportType,
            signature,
            normalizedDataVersion);
    }

    @Transactional(transactionManager = PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    public CreateOrReuseResult createPendingOrReuse(ReportType reportType,
                                                    String fileType,
                                                    LocalDate dataVersion,
                                                    Map<String, String> params) {

        LocalDate normalizedDataVersion = dataVersion == null ? LocalDate.now(ZoneOffset.UTC) : dataVersion;
        String normalizedFileType = normalizeFileType(fileType);
        String signature = ReportRequestSignature.from(reportType.name(), normalizedFileType, params);

        Optional<ReportDownloadRequest>
            existing =
            this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                reportType,
                signature,
                normalizedDataVersion);

        if (existing.isPresent() && !FileDownloadStatus.FAILED.equals(existing.get()
                                                                              .getStatus())) {

            return new CreateOrReuseResult(new ReportDownloadRequestData(existing.get()), true, signature);
        }

        if (existing.isPresent()) {

            if (!this.canRetryFailedRequest(existing.get())) {

                return new CreateOrReuseResult(new ReportDownloadRequestData(existing.get()), true, signature);
            }

            ReportDownloadRequest retried = this.resetFailedToPending(existing.get());
            return new CreateOrReuseResult(new ReportDownloadRequestData(retried), false, signature);
        }

        Instant now = Instant.now();
        ReportDownloadRequest request = new ReportDownloadRequest(
            reportType,
            signature,
            normalizedDataVersion,
            FileDownloadStatus.PENDING,
            normalizedFileType,
            now,
            now);

        try {

            this.reportDownloadRequestRepository.save(request);

            params.forEach((key, value) -> this.reportDownloadRequestParamRepository.save(
                new ReportDownloadRequestParam(request.getId(), key, value, now)));

            return new CreateOrReuseResult(new ReportDownloadRequestData(request), false, signature);

        } catch (DataIntegrityViolationException e) {

            ReportDownloadRequest
                reused =
                this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                        reportType,
                        signature,
                        normalizedDataVersion)
                                                    .orElseThrow(() -> e);

            if (FileDownloadStatus.FAILED.equals(reused.getStatus())) {

                if (!this.canRetryFailedRequest(reused)) {

                    return new CreateOrReuseResult(new ReportDownloadRequestData(reused), true, signature);
                }

                ReportDownloadRequest retried = this.resetFailedToPending(reused);
                return new CreateOrReuseResult(new ReportDownloadRequestData(retried), false, signature);
            }

            return new CreateOrReuseResult(new ReportDownloadRequestData(reused), true, signature);
        }
    }

    private String normalizeFileType(String fileType) {

        return fileType == null ? "" : fileType.trim()
                                               .toLowerCase(Locale.ROOT);
    }

    private ReportDownloadRequest resetFailedToPending(ReportDownloadRequest failedRequest) {

        Instant now = Instant.now();
        failedRequest.status(FileDownloadStatus.PENDING);
        failedRequest.fileUrl(null);
        failedRequest.errorMessage(null);
        failedRequest.finishedDate(null);
        failedRequest.retryCount(this.retryCount(failedRequest) + 1);
        failedRequest.updatedDate(now);
        return this.reportDownloadRequestRepository.save(failedRequest);
    }

    private boolean canRetryFailedRequest(ReportDownloadRequest request) {

        String error = request.getErrorMessage();

        if (error == null || error.isBlank()) {

            return this.retryCount(request) < MAX_RETRY_COUNT;
        }

        String errorCode = this.extractErrorCode(error);

        if (this.isKnownReportError(errorCode)) {

            return false;
        }

        return this.retryCount(request) < MAX_RETRY_COUNT;
    }

    private String extractErrorCode(String error) {

        int delimiterIndex = error.indexOf("-");
        return delimiterIndex > 0 ? error.substring(0, delimiterIndex) : error;
    }

    private boolean isKnownReportError(String errorCode) {

        return ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION.getCode()
                                                             .equals(errorCode) ||
                   ReportErrors.RESULT_NOT_FOUND_EXCEPTION.getCode()
                                                          .equals(errorCode);
    }

    private int retryCount(ReportDownloadRequest request) {

        Integer value = request.getRetryCount();
        return value == null ? 0 : value;
    }

    public record CreateOrReuseResult(ReportDownloadRequestData request,
                                      boolean reused,
                                      String paramsSignature) {

    }

}
