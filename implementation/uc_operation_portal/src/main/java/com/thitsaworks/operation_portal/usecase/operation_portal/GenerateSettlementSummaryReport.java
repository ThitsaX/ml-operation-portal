package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementSummaryReport
    extends UseCase<GenerateSettlementSummaryReport.Input, GenerateSettlementSummaryReport.Output> {

    record Input(String fspId,
                 String settlementId,
                 String fileType,
                 String timezoneOffset,
                 Long userId) {}

    record Output(byte[] settlementByte) { }

}
