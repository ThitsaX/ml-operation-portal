package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementBankReport extends
                                              UseCase<GenerateSettlementBankReport.Input, GenerateSettlementBankReport.Output> {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezone,
                 String user) {}

    record Output(byte[] reportData) {}

}
