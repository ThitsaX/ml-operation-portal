package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateSettlementModel extends
                                       UseCase<CreateSettlementModel.Input, CreateSettlementModel.Output> {

    record Input(String settlementModelName,
                 String modelType,
                 String currencyID,
                 String zoneId,
                 boolean requireLiquidityCheck,
                 boolean autoPositionReset,
                 boolean adjustPosition) {

        public record SchedulerConfigInfo(String name, String description, String cronExpression, String zoneId){}

    }

    record Output(boolean created, SettlementModelId settlementModelId) {}

}
