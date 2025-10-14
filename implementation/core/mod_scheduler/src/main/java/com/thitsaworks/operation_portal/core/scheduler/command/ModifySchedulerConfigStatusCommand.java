package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface ModifySchedulerConfigStatusCommand {

    record Input(SchedulerConfigId schedulerConfigId, boolean active) {}

    record Output(boolean modified, SchedulerConfigData schedulerConfigData) {}

    Output execute(Input input) throws DomainException;

}
