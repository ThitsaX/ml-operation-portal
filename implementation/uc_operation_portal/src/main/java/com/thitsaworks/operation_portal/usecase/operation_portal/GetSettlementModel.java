package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;
import java.util.Set;

public interface GetSettlementModel extends UseCase<GetSettlementModel.Input, GetSettlementModel.Output> {

    public record Input() {

    }

    public record Output(List<SettlementModelData> settlementModels) {

        public record SettlementModelData(
                SettlementModelId settlementModelId,
                String name,
                String currencyId,
                boolean isActive,
                boolean autoCloseWindow,
                boolean requireLiquidityCheck,
                boolean autoPositionReset,
                boolean adjustPosition,
                Set<SchedulerConfigId> schedulerConfigIds) {

        }

    }

}
