package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateSettlementAuditReport extends
                                               UseCase<GenerateSettlementAuditReport.Input, GenerateSettlementAuditReport.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String dfspId,
                 String currencyId,
                 String fileType,
                 String timezone) {}

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  boolean reused,
                  String paramsSignature) {}

}
