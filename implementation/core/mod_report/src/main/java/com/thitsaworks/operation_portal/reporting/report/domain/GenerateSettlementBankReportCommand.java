package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementBankReportCommand {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezoneOffset,
                 String userName,
                 String dfspId,
                 boolean isParent,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] settlementBankRptByte) { }

    Output execute(Input input) throws ReportException;

    record CountInput(String settlementId, String currencyId) { }

    int countRows(CountInput input);

}
