package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.ZoneId;

public interface CreateSchedulerConfig
    extends UseCase<CreateSchedulerConfig.Input, CreateSchedulerConfig.Output> {

    record Input(String name, String jobName, String description, String cronExpression, ZoneId zoneId) {}

    record Output(boolean created) { }

}
