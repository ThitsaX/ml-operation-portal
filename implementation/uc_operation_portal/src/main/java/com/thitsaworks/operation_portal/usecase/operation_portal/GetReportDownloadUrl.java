package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GetReportDownloadUrl
    extends UseCase<GetReportDownloadUrl.Input, GetReportDownloadUrl.Output> {

    record Input(ReportDownloadRequestId requestId) { }

    record Output(FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  boolean found) { }
}
