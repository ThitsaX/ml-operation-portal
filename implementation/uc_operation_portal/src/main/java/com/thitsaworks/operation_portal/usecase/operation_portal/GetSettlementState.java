package com.thitsaworks.operation_portal.usecase.operation_portal;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;

import java.util.List;

public interface GetSettlementState extends UseCase<GetSettlementState.Input, GetSettlementState.Output> {

    record Input() {

    }

    record Output(List<SettlementStateData> settlementStates) {

        record SettlementState(String settlementStateId,
                               String enumeration) {

        }

    }

}
