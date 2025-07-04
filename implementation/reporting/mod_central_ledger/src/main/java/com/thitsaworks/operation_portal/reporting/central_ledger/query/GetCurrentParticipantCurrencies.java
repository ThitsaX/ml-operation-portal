package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerFailureException;
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

    Output execute(Input input) throws CentralLedgerFailureException;

}
