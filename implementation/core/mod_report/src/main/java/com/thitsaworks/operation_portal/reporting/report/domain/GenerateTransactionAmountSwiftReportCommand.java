package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateTransactionAmountSwiftReportCommand {

    record Input(String settlementId,
                 String currency,
                 String timezone
    ) {
    }

    record Output(byte[] feeSettlementRptByte) { }

    Output execute(Input input) throws ReportException;

}
