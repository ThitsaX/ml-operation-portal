package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetSettlementWindowStateList
        extends UseCase<GetSettlementWindowStateList.Input, GetSettlementWindowStateList.Output>{

    record Input() {

    }


    record Output(
            List<SettlementWindowStateData> settlementWindowStates ) {



        public record SettlementWindowStateData(String settlementWindowId,
                                                String enumeration) {


        }

    }

}
