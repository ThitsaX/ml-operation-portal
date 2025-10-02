package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifySchedulerConfig
    extends UseCase<ModifySchedulerConfig.Input, ModifySchedulerConfig.Output> {

    record Input(SchedulerConfigId schedulerConfigId, String name, String jobName, String cronExpression,
                 String description, boolean active) {}

    record Output(boolean updated) { }

}
