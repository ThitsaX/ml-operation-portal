package com.thitsaworks.operation_portal.core.reporting.download.query;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.data.ReportDownloadRequestData;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportDownloadRequestQuery {

    FileDownloadStatus getStatus(ReportDownloadRequestId id) throws ReportException;

    Optional<ReportDownloadRequestData> findTopByStatusOrderByCreatedAtAsc(FileDownloadStatus status);

    Optional<ReportDownloadRequestData> findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
        ReportType reportType,
        String paramsSignature,
        LocalDate dataVersion);
}
