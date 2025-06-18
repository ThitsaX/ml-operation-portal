package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import java.time.Instant;

public abstract class GenerateStatementReport extends
        AbstractAuditableUseCase<GenerateStatementReport.Input, GenerateStatementReport.Output> {

    public record Input(
            Instant startDate,
            Instant endDate,
            String fspId,
            String fileType,
            String timezoneOffSet,
            String currencyId
    ) {}

    public record Output(byte[] statementData) {}
}
