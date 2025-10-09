package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementBankReportCommand {

    record Input(
        String settlementId,
        String currencyId,
        String fileType,
        String timezoneOffset
    ) { }

    record Output(
        byte[] settlementBankReportByte
    ) { }

    Output execute(Input input) throws ReportException;

}
