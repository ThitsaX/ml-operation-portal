package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

import java.util.List;

public interface GetSettlementSchedulerList
        extends UseCase<GetSettlementSchedulerList.Input, GetSettlementSchedulerList.Output> {

    public record Input(SettlementModelId settlementModelId) {

    }

    public record Output(List<SchedulerConfigData> schedulerConfigDataList) {}

}
