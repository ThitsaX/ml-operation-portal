package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementReportCommand {

    record Input(String fspId,
                 String fspName,
                 String settlementId,
                 String filetype,
                 String timezoneOffset, String userName) { }

    record Output(byte[] settlementRptByte) { }

    Output execute(Input input) throws ReportException;

}
