package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.ledger.data.CurrencyData;
import com.thitsaworks.operation_portal.component.usecase.AbstractOwnableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class GetParticipantCurrencies extends
        AbstractOwnableUseCase<GetParticipantCurrencies.Input, GetParticipantCurrencies.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        String dfspId;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<CurrencyData> currencyDataList;

    }
}
