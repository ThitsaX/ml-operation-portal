package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementSummaryReport
    extends UseCase<GenerateSettlementSummaryReport.Input, GenerateSettlementSummaryReport.Output> {

    record Input(String fspId,
                 String settlementId,
                 String fileType,
                 String timezoneOffset,
                 Long userId) {}

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  boolean reused,
                  String paramsSignature) { }

}
