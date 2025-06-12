package com.thitsaworks.operation_portal.central_ledger.report.domain;

import lombok.Value;

import java.time.Instant;

public interface GenerateFeeSettlementReportCommand {

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
