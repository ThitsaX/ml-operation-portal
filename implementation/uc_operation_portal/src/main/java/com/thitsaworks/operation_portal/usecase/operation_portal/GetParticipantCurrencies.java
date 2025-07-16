package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.CurrencyData;

import java.util.List;

public interface GetParticipantCurrencies extends
                                          UseCase<GetParticipantCurrencies.Input, GetParticipantCurrencies.Output> {

    record Input(String dfspId) { }

    record Output(List<CurrencyData> currencyDataList) { }

}
