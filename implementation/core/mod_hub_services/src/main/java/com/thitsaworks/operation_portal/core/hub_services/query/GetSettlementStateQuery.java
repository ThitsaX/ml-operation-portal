package com.thitsaworks.operation_portal.core.hub_services.query;

import java.util.List;

public interface GetSettlementStateQuery {

    record Input() {

    }

    record Output(List<com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData> settlementStates) {

        record SettlementState(String settlementStateId,
                                   String enumeration) {

        }

    }

    Output execute(Input input);

}
