package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportFailureException;

import java.time.Instant;

public interface GenerateStatementReportCommand {

    record Input(Instant startDate,
                 Instant endDate,
                 String fspId,
                 String accountNumber,
                 String filetype,
                 String timeZoneOffset,
                 String currencyId) { }

    record Output(byte[] statementRptData) { }

    Output execute(Input input) throws ReportFailureException;

}
