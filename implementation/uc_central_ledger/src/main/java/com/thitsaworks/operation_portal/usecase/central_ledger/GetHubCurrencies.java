package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;

import java.util.List;

public interface GetHubCurrencies extends
                                  UseCase<GetHubCurrencies.Input, GetHubCurrencies.Output> {

    record Input() { }

    record Output(List<CurrencyData> currencyDataList) { }

}
