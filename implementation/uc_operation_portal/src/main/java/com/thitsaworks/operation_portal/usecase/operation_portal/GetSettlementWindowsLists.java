package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsList.SettlementWindow;

import java.util.List;

public interface GetSettlementWindowsLists
        extends UseCase<GetSettlementWindowsLists.Input, GetSettlementWindowsLists.Output> {
    public record Input(String fromDate,
                        String toDate,
                        String currency,
                        String state,
                        Integer participantId){
    }

    public record Output(List<SettlementWindow> settlementWindows) {}
    }

