package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import lombok.Value;

import java.util.List;

public interface GetCurrenciesQuery {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<CurrencyData> currencyDataList;

    }

    GetCurrenciesQuery.Output execute(Input input) throws CentralLedgerException;

}
