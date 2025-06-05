package com.thitsa.dfsp_portal.report.query;

import lombok.Value;

public interface GenerateDetailRpt {

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
