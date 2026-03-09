package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateSettlementStatementReportCommand {

    record Input(String fspId,
                 String dfspName,
                 Instant startDate,
                 Instant endDate,
                 String filetype,
                 String currencyId,
                 String timeZoneOffset,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] settlementStatementRptByte) { }

    Output execute(Input input) throws ReportException;

    record CountInput(String fspId,
                      Instant startDate,
                      Instant endDate,
                      String currencyId,
                      String timeZoneOffset) { }

    int countRows(CountInput input);

}
