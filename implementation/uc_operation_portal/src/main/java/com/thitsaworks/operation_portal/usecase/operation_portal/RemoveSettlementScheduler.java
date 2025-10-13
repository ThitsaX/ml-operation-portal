package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveSettlementScheduler extends
                                           UseCase<RemoveSettlementScheduler.Input, RemoveSettlementScheduler.Output> {

    record Input(SettlementModelId settlementModelId,
                 SchedulerConfigId schedulerConfigId) {

    }

    record Output(boolean removed, SchedulerConfigId schedulerConfigId) {}

}
