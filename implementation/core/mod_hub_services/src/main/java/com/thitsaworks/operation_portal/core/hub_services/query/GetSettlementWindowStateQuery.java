package com.thitsaworks.operation_portal.core.hub_services.query;

import java.util.List;

public interface GetSettlementWindowStateQuery {

    record Input() {

    }

    record Output(List<com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData> settlementWindowStates ) {



        record SettlementWindowStateData(String settlementWindowId,
                                         String enumeration) {


        }

    }

    Output execute(Input input);

}
