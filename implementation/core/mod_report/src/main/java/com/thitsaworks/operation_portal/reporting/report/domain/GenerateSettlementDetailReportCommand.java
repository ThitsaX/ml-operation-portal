package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementDetailReportCommand {

    record Input(String settlementId,
                 String fspId,
                 String dfspName,
                 String fileType,
                 String timezoneOffset) { }

    record Output(byte[] settlementDetailRptByte) { }

    Output execute(Input input) throws ReportException;

}
