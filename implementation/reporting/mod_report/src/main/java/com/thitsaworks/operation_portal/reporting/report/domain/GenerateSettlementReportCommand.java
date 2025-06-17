package com.thitsaworks.operation_portal.reporting.report.domain;

import lombok.Value;

public interface GenerateSettlementReportCommand {

    @Value
    class Input {

        private String fspId;

        private String settlementId;

        private String filetype;

        private String timezoneOffset;

    }

    @Value
    class Output {

        private byte[] settlementByte;

    }

    GenerateSettlementReportCommand.Output execute(GenerateSettlementReportCommand.Input input) throws Exception;

}
