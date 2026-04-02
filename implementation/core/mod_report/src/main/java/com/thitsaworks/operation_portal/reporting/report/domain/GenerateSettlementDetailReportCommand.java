package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementDetailReportCommand {

    record Input(String settlementId,
                 String fspId,
                 String dfspName,
                 String fileType,
                 String timezoneOffset,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] settlementDetailRptByte) { }

    Output execute(Input input) throws ReportException;

    record CountInput(String settlementId, String fspId) { }

    int countRows(CountInput input);

}
