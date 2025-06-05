package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GenerateSettlementReport
        extends AbstractAuditableUseCase<GenerateSettlementReport.Input, GenerateSettlementReport.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String fspId;

        private String settlementId;

        private String fileType;

        private String timezoneOffset;

      //  private String destination;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private byte[] settlementByte;

    }

}
