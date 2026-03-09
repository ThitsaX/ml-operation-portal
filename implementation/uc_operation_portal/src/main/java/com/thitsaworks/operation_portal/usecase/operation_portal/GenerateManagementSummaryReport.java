package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateManagementSummaryReport extends UseCase<GenerateManagementSummaryReport.Input, GenerateManagementSummaryReport.Output> {

    record Input(String startDate, String endDate, String timezoneOffset ,String fileType,Long userId) {}

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  boolean reused,
                  String paramsSignature) {}
}
