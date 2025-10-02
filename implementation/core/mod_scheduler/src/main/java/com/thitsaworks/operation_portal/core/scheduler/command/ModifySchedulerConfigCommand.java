package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface ModifySchedulerConfigCommand {

    record Input(SchedulerConfigId schedulerConfigId, String name, String jobName, String cronExpression,
                 String description, boolean active) {}

    record Output(SchedulerConfigData schedulerConfigData) {}

    Output execute(Input input) throws DomainException;

}
