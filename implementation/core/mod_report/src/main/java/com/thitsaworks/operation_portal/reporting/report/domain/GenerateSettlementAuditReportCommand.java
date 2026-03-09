package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateSettlementAuditReportCommand {

    record Input(Instant startDate,
                 Instant endDate,
                 String dfspId,
                 String dfspName,
                 String currencyId,
                 String filetype,
                 String timeZoneOffset,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] settlementAuditRptByte) { }

    Output execute(Input input) throws ReportException;

    record CountInput(Instant startDate,
                      Instant endDate,
                      String dfspId,
                      String currencyId,
                      String timeZoneOffset) { }

    int countRows(CountInput input);

}
