package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.CurrencyData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
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

    GetCurrenciesQuery.Output execute(Input input) throws HubServicesException;

}
