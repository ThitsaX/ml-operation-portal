package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
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
