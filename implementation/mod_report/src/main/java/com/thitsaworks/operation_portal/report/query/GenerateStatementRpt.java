package com.thitsaworks.operation_portal.report.query;

import lombok.Value;

import java.time.Instant;

public interface GenerateStatementRpt {

    @Value
    class Input {

        private Instant startDate;

        private Instant endDate;

        private String fspId;

        private String filetype;

        private String timeZoneOffset;

        private String currencyId;

    }

    @Value
    class Output {

      private   byte[] statementRptData;

    }

    Output execute(Input input) throws Exception;

}
