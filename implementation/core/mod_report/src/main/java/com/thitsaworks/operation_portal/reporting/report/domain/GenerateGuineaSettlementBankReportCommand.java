package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateGuineaSettlementBankReportCommand {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezoneOffset,
                 String userName,
                 String dfspId) {}

    record Output(byte[] settlementBankRptByte) { }

    Output execute(Input input) throws ReportException;

}
