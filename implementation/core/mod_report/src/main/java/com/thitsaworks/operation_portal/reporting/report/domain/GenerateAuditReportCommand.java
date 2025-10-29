package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;
import java.util.List;

public interface GenerateAuditReportCommand {

    Output execute(Input input) throws ReportException;

    record Input(String realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneOffset,
                 String userId,
                 String actionId,
                 String fileType,
                 List<String> grantedActionList) { }

    record Output(byte[] auditRptByte) { }

}
