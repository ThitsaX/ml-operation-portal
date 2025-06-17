package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class GetHubCurrencies extends
                                       AbstractOwnableUseCase<GetHubCurrencies.Input, GetHubCurrencies.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<CurrencyData> currencyDataList;

    }
}
