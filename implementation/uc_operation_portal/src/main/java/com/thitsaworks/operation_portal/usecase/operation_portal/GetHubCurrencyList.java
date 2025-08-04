package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.CurrencyData;

import java.util.List;

public interface GetHubCurrencyList extends
                                  UseCase<GetHubCurrencyList.Input, GetHubCurrencyList.Output> {

    record Input() { }

    record Output(List<CurrencyData> currencyDataList) { }

}
