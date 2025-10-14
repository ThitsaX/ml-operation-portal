package com.thitsaworks.operation_portal.usecase.operation_portal;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetSettlementStateList extends UseCase<GetSettlementStateList.Input, GetSettlementStateList.Output> {

    record Input() {

    }

    record Output(List<SettlementStateData> settlementStates) {

        public record SettlementStateData(String settlementStateId,
                                          String enumeration) {

        }

    }

}
