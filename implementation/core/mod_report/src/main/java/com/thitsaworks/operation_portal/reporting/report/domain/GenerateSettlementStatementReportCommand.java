package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateSettlementStatementReportCommand {

    record Input(String fspId,
                 Instant startDate,
                 Instant endDate,
                 String filetype,
                 String currencyId,
                 String timeZoneOffset) { }

    record Output(byte[] statementRptData) { }

    Output execute(Input input) throws ReportException;

}
