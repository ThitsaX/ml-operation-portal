package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifySettlementModel extends
                                       UseCase<ModifySettlementModel.Input, ModifySettlementModel.Output> {

    record Input(SettlementModelId settlementModelId,
                 String settlementModelName,
                 String modelType,
                 String currencyID,
                 boolean isActive,
                 boolean autoCloseWindow) {

    }

    record Output(boolean modified, SettlementModelId settlementModelId) {}

}
