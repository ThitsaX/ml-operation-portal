package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
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
