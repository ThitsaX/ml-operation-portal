package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

public interface ModifySchedulerConfigStatusCommand {

    record Input(SchedulerConfigId schedulerConfigId, boolean active) {}

    record Output(boolean modified, SchedulerConfigId schedulerConfigId) {}

    Output execute(Input input) throws DomainException;

}
