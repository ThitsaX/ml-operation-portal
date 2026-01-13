package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateManagementSummaryReportCommand {

    record Input(String startDate,
                 String endDate,
                 String timezoneOffset,
                 String fileType,
                 String userName) { }

    record Output(byte[] managementSummaryRptByte) { }

    Output execute(Input input) throws ReportException;
}

