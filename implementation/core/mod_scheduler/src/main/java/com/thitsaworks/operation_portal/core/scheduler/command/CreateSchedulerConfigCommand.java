package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface CreateSchedulerConfigCommand {

    record Input(String name, String jobName, String description, String cronExpression, String zoneId) {}

    record Output(SchedulerConfigData schedulerConfigData) {}

    Output execute(Input input) throws DomainException;

}
