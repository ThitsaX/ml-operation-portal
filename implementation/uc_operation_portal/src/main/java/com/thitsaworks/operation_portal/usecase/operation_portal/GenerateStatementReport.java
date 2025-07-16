package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateStatementReport extends
                                         UseCase<GenerateStatementReport.Input, GenerateStatementReport.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String fspId,
                 String fileType,
                 String timezoneOffSet,
                 String currencyId) { }

    record Output(byte[] statementData) { }

}
