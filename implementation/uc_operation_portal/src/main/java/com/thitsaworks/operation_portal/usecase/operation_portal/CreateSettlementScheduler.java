package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface CreateSettlementScheduler extends
                                           UseCase<CreateSettlementScheduler.Input, CreateSettlementScheduler.Output> {

    record Input(SettlementModelId settlementModelId,
                 List<SchedulerConfigInfo> schedulerConfigInfoList) {

        public record SchedulerConfigInfo(String name,
                                          String description,
                                          String cronExpression,
                                          String zoneId) { }

    }

    record Output(boolean created) { }

}
