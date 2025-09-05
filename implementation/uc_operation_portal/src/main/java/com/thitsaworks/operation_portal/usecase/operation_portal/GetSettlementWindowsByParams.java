package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsByParams.SettlementWindow;

import java.util.List;

public interface GetSettlementWindowsByParams
        extends UseCase<GetSettlementWindowsByParams.Input, GetSettlementWindowsByParams.Output> {
    public record Input(String fromDate,
                        String toDate,
                        String currency,
                        String state,
                        Integer participantId){
    }

    public record Output(List<SettlementWindow> settlementWindows) {}
    }

