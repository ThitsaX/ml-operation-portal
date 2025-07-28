package com.thitsaworks.operation_portal.core.scheduler.command;

public interface DeleteSchedulerConfigCommand {

    record Output(boolean deleted) {}

    Output execute(Long input);

}
