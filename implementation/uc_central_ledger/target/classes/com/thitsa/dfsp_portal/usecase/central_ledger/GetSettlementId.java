package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsa.dfsp_portal.report.domain.data.SettlementIdData;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

public abstract class GetSettlementId extends
        AbstractOwnableUseCase<GetSettlementId.Input, GetSettlementId.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private Instant startDate;

        private Instant endDate;

        private String timezoneOffset;


    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<SettlementIdData> settlementIds;

    }

}
