package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

public interface DeleteSchedulerConfigCommand {

    record Output(boolean deleted) {}

    Output execute(SchedulerConfigId input) throws DomainException;

}
