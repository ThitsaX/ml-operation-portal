package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GetReportDownloadStatus
        extends UseCase<GetReportDownloadStatus.Input, GetReportDownloadStatus.Output> {

    record Input(ReportDownloadRequestId requestId) { }

    record Output(FileDownloadStatus status, boolean found) { }
}
