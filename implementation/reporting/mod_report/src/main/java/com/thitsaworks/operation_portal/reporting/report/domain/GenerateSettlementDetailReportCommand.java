package com.thitsaworks.operation_portal.reporting.report.domain;

public interface GenerateSettlementDetailReportCommand {

    record Input(
            String settlementId,
            String fspId,
            String fileType,
            String timezoneOffset
    ) {}

    record Output(
            byte[] detailReportByte
    ) {}

    Output execute(Input input) throws Exception;

}
