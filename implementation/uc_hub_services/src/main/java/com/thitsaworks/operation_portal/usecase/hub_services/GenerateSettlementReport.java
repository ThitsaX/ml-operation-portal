package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementReport
    extends UseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output> {

    record Input(String fspId,
                 String settlementId,
                 String fileType,
                 String timezoneOffset) { }

    record Output(byte[] settlementByte) { }

}
