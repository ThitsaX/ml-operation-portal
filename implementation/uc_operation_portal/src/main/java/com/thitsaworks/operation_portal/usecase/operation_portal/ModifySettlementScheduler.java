package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifySettlementScheduler
        extends UseCase<ModifySettlementScheduler.Input, ModifySettlementScheduler.Output> {

    record Input(SettlementModelId settlementModelId, SchedulerConfigId schedulerConfigId, String name,
                 String description, String cronExpression, boolean active) {}

    record Output(boolean updated) {}

}
