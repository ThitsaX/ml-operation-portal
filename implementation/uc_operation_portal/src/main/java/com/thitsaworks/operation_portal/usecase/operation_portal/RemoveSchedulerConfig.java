package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveSchedulerConfig
    extends UseCase<RemoveSchedulerConfig.Input, RemoveSchedulerConfig.Output> {

    record Input(SchedulerConfigId schedulerConfigId) { }

    record Output(boolean deleted) { }
}