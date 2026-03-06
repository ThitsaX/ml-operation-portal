package com.thitsaworks.operation_portal.core.reporting.download.data;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestParamId;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequestParam;

import java.time.Instant;

public record ReportDownloadRequestParamData(ReportDownloadRequestParamId requestParamId,
                                             ReportDownloadRequestId requestId,
                                             String paramKey,
                                             String paramValue,
                                             Instant createdAt,
                                             Instant updatedAt) {

    public ReportDownloadRequestParamData(ReportDownloadRequestParam requestParam) {

        this(requestParam.getRequestParamId(),
             requestParam.getRequestId(),
             requestParam.getParamKey(),
             requestParam.getParamValue(),
             requestParam.getCreatedAt(),
             requestParam.getUpdatedAt());
    }
}
