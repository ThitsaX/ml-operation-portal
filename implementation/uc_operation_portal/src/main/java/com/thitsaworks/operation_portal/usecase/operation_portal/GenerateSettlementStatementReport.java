package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateSettlementStatementReport
    extends UseCase<GenerateSettlementStatementReport.Input, GenerateSettlementStatementReport.Output> {

    record Input(String fspId,
                 String startDate,
                 String endDate,
                 String fileType,
                 String currencyId,
                 String timezoneOffSet) { }

    record Output(byte[] statementData) { }

}
