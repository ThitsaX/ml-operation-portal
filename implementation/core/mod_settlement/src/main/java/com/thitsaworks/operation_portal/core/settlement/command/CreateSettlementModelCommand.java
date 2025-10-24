package com.thitsaworks.operation_portal.core.settlement.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;

import java.util.List;

public interface CreateSettlementModelCommand {

    record Input(String name,
                 String type,
                 String currencyId,
                 boolean isActive,
                 boolean autoCloseWindow,
                 boolean manualCloseWindow,
                 String zoneId,
                 boolean requireLiquidityCheck,
                 boolean autoPositionReset,
                 boolean adjustPosition,
                 List<SchedulerConfigId> schedulerConfigIdList) {}

    record Output(boolean created,
                  SettlementModelId settlementModelId) {}

    Output execute(Input input) throws SettlementException;

}
