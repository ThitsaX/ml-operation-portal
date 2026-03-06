package com.thitsaworks.operation_portal.core.reporting.download.request;

import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.reporting.download.data.ReportDownloadRequestData;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequestParam;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestParamRepository;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestRepository;
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

        Optional<ReportDownloadRequest> existing = this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                reportType,
                signature,
                normalizedDataVersion);

        if (existing.isPresent() && !FileDownloadStatus.FAILED.equals(existing.get().getStatus())) {

            return new CreateOrReuseResult(new ReportDownloadRequestData(existing.get()), true, signature);
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

            ReportDownloadRequest reused = this.reportDownloadRequestRepository.findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
                    reportType,
                    signature,
                    normalizedDataVersion)
                                                                               .orElseThrow(() -> e);

            return new CreateOrReuseResult(new ReportDownloadRequestData(reused), true, signature);
        }
    }


    private String normalizeFileType(String fileType) {

        return fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
    }

    public record CreateOrReuseResult(ReportDownloadRequestData request,
                                      boolean reused,
                                      String paramsSignature) {

    }
}
