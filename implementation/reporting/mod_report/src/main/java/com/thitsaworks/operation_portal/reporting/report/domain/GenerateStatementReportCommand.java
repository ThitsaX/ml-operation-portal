package com.thitsaworks.operation_portal.reporting.report.domain;

import lombok.Value;

import java.time.Instant;

public interface GenerateStatementReportCommand {

    @Value
    class Input {

        private Instant startDate;

        private Instant endDate;

        private String fspId;

        private String accountNumber;

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
