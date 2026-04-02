package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementReportCommand {

    record Input(String fspId,
                 String fspName,
                 String settlementId,
                 String filetype,
                 String timezoneOffset,
                 String userName,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] settlementRptByte) { }

    Output execute(Input input) throws ReportException;

    default Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        throw new ReportException(ReportErrors.SETTLEMENT_REPORT_FAILURE_EXCEPTION);
    }

    record CountInput(String fspId, String settlementId) { }

    int countRows(CountInput input);

}
