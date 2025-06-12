package com.thitsaworks.operation_portal.central_ledger.report.domain;

import lombok.Value;

public interface GenerateSettlementDetailReportCommand {

    @Value
    class Input {

        private String settlementId;

        private String fspId;

        private String fileType;

        private String timezoneOffset;

    }

    @Value
    class Output {

    private byte[] detailReportByte;

    }

    Output execute(Input input) throws Exception;

}
