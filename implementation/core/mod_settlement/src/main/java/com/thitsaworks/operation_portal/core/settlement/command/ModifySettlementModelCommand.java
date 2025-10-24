package com.thitsaworks.operation_portal.core.settlement.command;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;

public interface ModifySettlementModelCommand {

    record Input(SettlementModelId settlementModelId,
                 String name,
                 String type,
                 String currencyId,
                 boolean isActive,
                 boolean autoCloseWindow,
                 boolean manualCloseWindow,
                 String zoneId) {}

    record Output(boolean modified, SettlementModelId settlementModelId) {}

    Output execute(Input input) throws SettlementException;

}
