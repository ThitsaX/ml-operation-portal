package com.thitsa.dfsp_portal.report.query;

import lombok.Value;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public interface GenerateFeeSettlementRpt {

    @Value
    class Input {

        private Instant startDate;

        private Instant endDate;

        private String fromFsp;

        private String toFsp;

        private String currency;

        private String timezone;

        private String fileType;
    }

    @Value
    class Output {

    private byte[] rptData;

    }

    Output execute(Input input) throws Exception;

}
