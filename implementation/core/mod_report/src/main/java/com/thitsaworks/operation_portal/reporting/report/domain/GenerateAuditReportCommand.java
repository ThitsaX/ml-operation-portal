package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateAuditReportCommand {

    Output execute(Input input) throws ReportException;

    record Input(String realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneoffset,
                 String userId,
                 String action,
                 String fileType) { }

    record Output(byte[] rptData) { }

}
