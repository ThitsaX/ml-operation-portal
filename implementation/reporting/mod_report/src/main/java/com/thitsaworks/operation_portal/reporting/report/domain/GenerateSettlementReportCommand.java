package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportFailureException;

public interface GenerateSettlementReportCommand {

    record Input(String fspId,
                 String settlementId,
                 String filetype,
                 String timezoneOffset) { }

    record Output(byte[] settlementByte) { }

    Output execute(Input input) throws ReportFailureException;

}
