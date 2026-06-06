package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateFeeSummaryReportCommand {

    record Input(Instant startDate,
                 Instant endDate,
                 String fspId,
                 String filetype,
                 String timeZoneOffset,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] feeSummaryRptByte) { }

    Output execute(Input input) throws ReportException;

    default Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        throw new ReportException(ReportErrors.FEE_SUMMARY_REPORT_FAILURE_EXCEPTION);
    }

    record CountInput(Instant startDate,
                      Instant endDate,
                      String dfspId,
                      String timeZoneOffset) { }

    int countRows(CountInput input);

}
