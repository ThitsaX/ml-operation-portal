package com.thitsaworks.operation_portal.core.scheduler.command;

public interface ScheduleTaskCommand {

    record Output(boolean scheduled) {}

    void destroy();

    Output execute(String taskName, String cronExpression);

}
