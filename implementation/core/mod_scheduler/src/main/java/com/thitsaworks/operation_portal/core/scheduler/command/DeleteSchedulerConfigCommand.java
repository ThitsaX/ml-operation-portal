package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface DeleteSchedulerConfigCommand {

    record Output(boolean deleted, SchedulerConfigData schedulerConfigData) {}

    Output execute(SchedulerConfigId input) throws DomainException;

}
