package com.thitsaworks.operation_portal.core.report_download.request;

import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequestParam;
import com.thitsaworks.operation_portal.core.report_download.model.repository.ReportDownloadRequestParamRepository;
import com.thitsaworks.operation_portal.core.report_download.model.repository.ReportDownloadRequestRepository;
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

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_FAILED = "FAILED";

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;
    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    public ReportDownloadRequestManager(ReportDownloadRequestRepository reportDownloadRequestRepository,
                                        ReportDownloadRequestParamRepository reportDownloadRequestParamRepository) {

        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
        this.reportDownloadRequestParamRepository = reportDownloadRequestParamRepository;
    }

    public Optional<ReportDownloadRequest> findExistingRequest(String reportType,
                                                               String fileType,
                                                               LocalDate dataVersion,
                                                               Map<String, String> params) {

        String normalizedReportType = normalizeReportType(reportType);
        String normalizedFileType = normalizeFileType(fileType);
        LocalDate normalizedDataVersion = dataVersion == null ? LocalDate.now(ZoneOffset.UTC) : dataVersion;
        String signature = ReportRequestSignature.from(normalizedReportType, normalizedFileType, params);

        return this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                normalizedReportType,
                signature,
                normalizedDataVersion);
    }

    @Transactional
    public CreateOrReuseResult createPendingOrReuse(String reportType,
                                                     String fileType,
                                                     LocalDate dataVersion,
                                                     Map<String, String> params) {

        LocalDate normalizedDataVersion = dataVersion == null ? LocalDate.now(ZoneOffset.UTC) : dataVersion;
        String normalizedReportType = normalizeReportType(reportType);
        String normalizedFileType = normalizeFileType(fileType);
        String signature = ReportRequestSignature.from(normalizedReportType, normalizedFileType, params);

        Optional<ReportDownloadRequest> existing = this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                normalizedReportType,
                signature,
                normalizedDataVersion);

        if (existing.isPresent() && !STATUS_FAILED.equalsIgnoreCase(existing.get().getStatus())) {

            return new CreateOrReuseResult(existing.get(), true, signature);
        }

        Instant now = Instant.now();
        ReportDownloadRequest request = new ReportDownloadRequest(
                normalizedReportType,
                signature,
                normalizedDataVersion,
                STATUS_PENDING,
                normalizedFileType,
                now,
                now);

        try {

            this.reportDownloadRequestRepository.save(request);

            params.forEach((key, value) -> this.reportDownloadRequestParamRepository.save(
                    new ReportDownloadRequestParam(request.getId(), key, value, now)));

            return new CreateOrReuseResult(request, false, signature);

        } catch (DataIntegrityViolationException e) {

            ReportDownloadRequest reused = this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                    normalizedReportType,
                    signature,
                    normalizedDataVersion)
                                                                               .orElseThrow(() -> e);

            return new CreateOrReuseResult(reused, true, signature);
        }
    }

    private String normalizeReportType(String reportType) {

        return reportType == null ? "" : reportType.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeFileType(String fileType) {

        return fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
    }

    public record CreateOrReuseResult(ReportDownloadRequest request,
                                      boolean reused,
                                      String paramsSignature) {

    }
}
