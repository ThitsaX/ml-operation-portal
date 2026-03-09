package com.thitsaworks.operation_portal.reporting.report.domain;

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

    record CountInput(String startDate, String endDate) { }

    int countRows(CountInput input);

}
