package com.thitsaworks.operation_portal.reporting.report.domain;

import lombok.Value;

public interface GenerateSettlementReportCommand {

    record Input(
            String fspId,
            String settlementId,
            String filetype,
            String timezoneOffset
    ) {}

    record Output(
            byte[] settlementByte
    ) {}

    GenerateSettlementReportCommand.Output execute(GenerateSettlementReportCommand.Input input) throws Exception;

}
