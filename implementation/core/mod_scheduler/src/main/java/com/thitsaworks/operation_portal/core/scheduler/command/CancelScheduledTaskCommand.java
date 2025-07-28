package com.thitsaworks.operation_portal.core.scheduler.command;

public interface CancelScheduledTaskCommand {
    record Output(boolean cancelled) {}
    Output execute(String taskName);
}