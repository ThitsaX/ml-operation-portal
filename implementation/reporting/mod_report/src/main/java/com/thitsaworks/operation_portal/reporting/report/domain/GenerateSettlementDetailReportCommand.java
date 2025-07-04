package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportFailureException;

public interface GenerateSettlementDetailReportCommand {

    record Input(
        String settlementId,
        String fspId,
        String fileType,
        String timezoneOffset
    ) { }

    record Output(
        byte[] detailReportByte
    ) { }

    Output execute(Input input) throws ReportFailureException;

}
