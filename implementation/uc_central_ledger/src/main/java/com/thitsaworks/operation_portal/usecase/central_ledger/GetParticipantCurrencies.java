package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import java.util.List;

public abstract class GetParticipantCurrencies extends
        AbstractOwnableUseCase<GetParticipantCurrencies.Input, GetParticipantCurrencies.Output> {

    public record Input(String dfspId) {}

    public record Output(List<CurrencyData> currencyDataList) {}
}
