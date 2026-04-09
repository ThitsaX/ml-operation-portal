package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementBankOverviewReport extends
                                              UseCase<GenerateSettlementBankOverviewReport.Input, GenerateSettlementBankOverviewReport.Output> {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezone,
                 Long userId) {}

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  String paramsSignature) {}

}
