package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

import java.time.Instant;

public abstract class GenerateFeeSettlementReport extends
        AbstractAuditableUseCase<GenerateFeeSettlementReport.Input, GenerateFeeSettlementReport.Output> {

    public record Input(
            Instant startDate,
            Instant endDate,
            String fromFsp,
            String toFsp,
            String currency,
            String timezone,
            String fileType
    ) {}

    public record Output(
            byte[] rptData
    ) {}

}
