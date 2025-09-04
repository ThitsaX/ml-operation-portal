package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.core.hub_services.api.GetHubSettlementWindows;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;


import java.util.List;

public interface GetSettlementWindows
    extends UseCase<GetSettlementWindows.Input, GetSettlementWindows.Output> {
    public record Input(String fromDate,
                        String toDate,
                        String currency,
                        String state,
                        Integer participantId){
    }
    public record Output(List<GetHubSettlementWindows.SettlementWindow> settlementWindows){}
    }

