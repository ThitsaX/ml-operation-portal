package com.thitsaworks.operation_portal.core.reporting.download.query;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.core.reporting.download.data.ReportDownloadRequestParamData;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequestParam;

import java.util.List;

public interface ReportDownloadRequestParamQuery {

    List<ReportDownloadRequestParamData> findByRequestId(ReportDownloadRequestId requestId);

}
