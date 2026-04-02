package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateManagementSummaryReportCommand {

    record Input(String startDate,
                 String endDate,
                 String timezoneOffset,
                 String fileType,
                 String userName,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] managementSummaryRptByte) { }

    Output execute(Input input) throws ReportException;

    default Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        throw new ReportException(ReportErrors.MANAGEMENT_SUMMARY_REPORT_FAILURE_EXCEPTION);
    }

    record CountInput(String startDate, String endDate) { }

    int countRows(CountInput input);

}
