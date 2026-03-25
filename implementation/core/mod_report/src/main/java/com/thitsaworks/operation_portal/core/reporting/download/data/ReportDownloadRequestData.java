package com.thitsaworks.operation_portal.core.reporting.download.data;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;

import java.time.Instant;

public record ReportDownloadRequestData(ReportDownloadRequestId requestId,
                                        ReportType reportType,
                                        String paramsSignature,
                                        FileDownloadStatus status,
                                        String fileType,
                                        String fileUrl,
                                        String errorMessage,
                                        Instant createdAt,
                                        Instant updatedAt,
                                        Instant finishedDate) {

    public ReportDownloadRequestData(ReportDownloadRequest request) {

        this(request.getRequestId(),
             request.getReportType(),
             request.getParamsSignature(),
             request.getStatus(),
             request.getFileType(),
             request.getFileUrl(),
             request.getErrorMessage(),
             request.getCreatedAt(),
             request.getUpdatedAt(),
             request.getFinishedDate());
    }
}
