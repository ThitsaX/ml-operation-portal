package com.thitsaworks.operation_portal.core.scheduler.command;

public interface InitializeScheduledTasksCommand {

    record Output(boolean initialized) {}

    Output execute(String taskName, String cronExpression);

}
