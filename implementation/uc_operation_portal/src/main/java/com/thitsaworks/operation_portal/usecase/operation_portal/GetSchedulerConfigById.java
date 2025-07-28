package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface GetSchedulerConfigById
    extends UseCase<GetSchedulerConfigById.Input, GetSchedulerConfigById.Output> {

    record Input(Long configId) { }

    record Output(SchedulerConfigData config) { }
}
