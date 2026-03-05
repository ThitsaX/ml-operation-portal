package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GetReportDownloadStatus
        extends UseCase<GetReportDownloadStatus.Input, GetReportDownloadStatus.Output> {

    record Input(Long requestId) { }

    record Output(Long requestId,
                  String reportType,
                  String status,
                  String fileUrl,
                  String errorMessage,
                  String finishedDate,
                  boolean found) { }
}
