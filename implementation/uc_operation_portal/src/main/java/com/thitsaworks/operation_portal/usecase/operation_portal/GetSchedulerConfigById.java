package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface GetSchedulerConfigById
    extends UseCase<GetSchedulerConfigById.Input, GetSchedulerConfigById.Output> {

    record Input(SchedulerConfigId schedulerConfigId) { }

    record Output(SchedulerConfigData config) { }
}
