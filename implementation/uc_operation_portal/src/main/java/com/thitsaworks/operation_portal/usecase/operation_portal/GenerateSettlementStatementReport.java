package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateSettlementStatementReport
    extends UseCase<GenerateSettlementStatementReport.Input, GenerateSettlementStatementReport.Output> {

    record Input(String fspId,
                 Instant startDate,
                 Instant endDate,
                 String fileType,
                 String currencyId,
                 String timezoneOffSet) { }

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  String paramsSignature) { }

}
