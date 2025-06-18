package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import java.util.List;

public abstract class GetHubCurrencies extends
        AbstractOwnableUseCase<GetHubCurrencies.Input, GetHubCurrencies.Output> {

    public record Input() {}

    public record Output(List<CurrencyData> currencyDataList) {}
}
