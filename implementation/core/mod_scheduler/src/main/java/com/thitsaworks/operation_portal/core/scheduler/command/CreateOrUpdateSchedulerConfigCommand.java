package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface CreateOrUpdateSchedulerConfigCommand {

    record Input(String name, String cronExpression, String description, boolean active) {}

    record Output(String cronExpression, boolean created) {}

    Output execute(Input input);

    void rescheduleTask(String taskName, String cronExpression);

}
