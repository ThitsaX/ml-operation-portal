package com.thitsaworks.operation_portal.core.settlement.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;

public interface AddSettlementSchedulerCommand {

    record Input(SettlementModelId settlementModelId, SchedulerConfigId schedulerConfigId) {}

    record Output(boolean created, SchedulerConfigId schedulerConfigId) {}

    Output execute(Input input) throws SettlementException;

}
