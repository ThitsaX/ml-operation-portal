package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateSettlementScheduler extends
                                           UseCase<CreateSettlementScheduler.Input, CreateSettlementScheduler.Output> {

    record Input(SettlementModelId settlementModelId,
                 String name,
                 String description,
                 String cronExpression) {
    }

    record Output(boolean created) { }

}
