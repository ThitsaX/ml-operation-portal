package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;

import java.util.List;

public interface GetSettlementWindowState extends UseCase<GetSettlementWindowState.Input, GetSettlementWindowState.Output>{

    record Input() {

    }


    record Output(
            List<com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData> settlementWindowStates ) {



        record SettlementWindowStateData(String settlementWindowId,
                                         String enumeration) {


        }

    }

}
