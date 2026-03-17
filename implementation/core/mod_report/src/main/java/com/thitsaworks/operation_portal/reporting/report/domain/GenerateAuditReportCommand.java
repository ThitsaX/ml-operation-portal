package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
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
                 List<String> grantedActionList,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] auditRptByte) { }

    record CountInput(String realmId,
                      Instant fromDate,
                      Instant toDate,
                      String userId,
                      String actionId,
                      List<String> grantedActionList) { }

    default Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        throw new ReportException(ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION);
    }

    int countRows(CountInput input);

}
