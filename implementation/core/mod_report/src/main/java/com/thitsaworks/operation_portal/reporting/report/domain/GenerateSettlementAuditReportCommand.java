package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
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

    default Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        throw new ReportException(ReportErrors.SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION);
    }

    record CountInput(Instant startDate,
                      Instant endDate,
                      String dfspId,
                      String currencyId,
                      String timeZoneOffset) { }

    int countRows(CountInput input);

}
