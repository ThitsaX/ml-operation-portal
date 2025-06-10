package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public abstract class GenerateFeeSettlementReport extends
        AbstractAuditableUseCase<GenerateFeeSettlementReport.Input, GenerateFeeSettlementReport.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private Instant startDate;

        private Instant endDate;

        private String fromFsp;

        private String toFsp;

        private String currency;

        private String timezone;


        private String fileType;

       // private String destination;


    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private byte[] RptData;

    }

}
