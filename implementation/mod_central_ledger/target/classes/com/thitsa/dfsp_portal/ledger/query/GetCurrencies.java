package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.CurrencyData;
import lombok.Value;

import java.util.List;

public interface GetCurrencies {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<CurrencyData> currencyDataList;

    }

    GetCurrencies.Output execute(Input input) throws Exception;

}
