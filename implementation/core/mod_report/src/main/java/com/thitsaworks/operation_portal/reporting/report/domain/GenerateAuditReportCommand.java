package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;
import java.util.List;

public interface GenerateAuditReportCommand {

    record CountInput(String realmId,
                      Instant fromDate,
                      Instant toDate,
                      String userId,
                      String actionId,
                      List<String> grantedActionList) { }

    Output execute(Input input) throws ReportException;

    record Input(String realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneOffset,
                 String userId,
                 String actionId,
                 String fileType,
                 List<String> grantedActionList,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] auditRptByte) { }

    int countRows(CountInput input);

    int auditPageSize();

}
