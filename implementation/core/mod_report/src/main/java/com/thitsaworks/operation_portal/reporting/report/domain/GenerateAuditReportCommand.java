package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.text.ParseException;
import java.time.Instant;

public interface GenerateAuditReportCommand {

    Output execute(Input input) throws ReportException;

    record Input(long realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneoffset) { }

    record Output(byte[] rptData) { }

}
