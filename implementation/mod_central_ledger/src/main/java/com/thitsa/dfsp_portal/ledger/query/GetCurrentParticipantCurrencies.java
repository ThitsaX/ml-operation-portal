package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.CurrencyData;
import lombok.Value;

import java.util.List;

public interface GetCurrentParticipantCurrencies {

    @Value
    class Input {

        String dfspId;

    }

    @Value
    class Output {

        private List<CurrencyData> currencyDataList;

    }

    GetCurrentParticipantCurrencies.Output execute(Input input) throws Exception;

}
