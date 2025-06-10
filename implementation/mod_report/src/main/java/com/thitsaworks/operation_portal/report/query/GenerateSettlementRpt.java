package com.thitsaworks.operation_portal.report.query;

import lombok.Value;

public interface GenerateSettlementRpt {

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

    GenerateSettlementRpt.Output execute(GenerateSettlementRpt.Input input) throws Exception;

}
