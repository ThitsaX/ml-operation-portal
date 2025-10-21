package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.ZoneId;

public interface ModifySettlementScheduler
        extends UseCase<ModifySettlementScheduler.Input, ModifySettlementScheduler.Output> {

    record Input(SettlementModelId settlementModelId, SchedulerConfigId schedulerConfigId, String name,
                 String description, String cronExpression, ZoneId zoneId, boolean active) {}

    record Output(boolean updated) {}

}
