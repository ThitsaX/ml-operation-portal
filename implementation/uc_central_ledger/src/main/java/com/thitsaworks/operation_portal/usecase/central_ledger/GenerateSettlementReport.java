package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

public abstract class GenerateSettlementReport
        extends AbstractAuditableUseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output> {

    public record Input(
            String fspId,
            String settlementId,
            String fileType,
            String timezoneOffset
    ) {}

    public record Output(byte[] settlementByte) {}
}
