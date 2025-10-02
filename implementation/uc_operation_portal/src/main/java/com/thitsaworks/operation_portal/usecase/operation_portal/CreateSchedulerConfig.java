package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateSchedulerConfig
    extends UseCase<CreateSchedulerConfig.Input, CreateSchedulerConfig.Output> {

    record Input(String name, String jobName, String cronExpression, String description) { }

    record Output(boolean created) { }

}
