package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public abstract class GenerateStatementReport extends
        AbstractAuditableUseCase<GenerateStatementReport.Input, GenerateStatementReport.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private Instant startDate;

        private Instant endDate;

        private String fspId;

        private String fileType;

        private String timezoneOffSet;

        private String currencyId;



    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private byte[] statementData;

    }

}
