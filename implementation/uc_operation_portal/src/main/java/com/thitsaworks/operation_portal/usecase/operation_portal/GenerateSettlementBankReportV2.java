package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementBankReportV2 extends
                                              UseCase<GenerateSettlementBankReportV2.Input, GenerateSettlementBankReportV2.Output> {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezone,
                 Long userId) {}

    record Output(byte[] reportData) {}

}
