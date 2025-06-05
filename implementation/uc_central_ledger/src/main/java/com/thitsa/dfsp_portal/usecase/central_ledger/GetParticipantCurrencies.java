package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsa.dfsp_portal.ledger.data.CurrencyData;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
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
