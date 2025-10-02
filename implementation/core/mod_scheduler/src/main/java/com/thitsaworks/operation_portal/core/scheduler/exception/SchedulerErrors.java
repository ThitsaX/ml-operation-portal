package com.thitsaworks.operation_portal.core.scheduler.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class SchedulerErrors {

    //@@formatter:off
    public static final ErrorMessage SCHEDULER_CONFIG_NOT_FOUND = new ErrorMessage("SCHEDULER_CONFIG_NOT_FOUND", "System cannot find the scheduler config with provided ID : [{0}].");
    public static final ErrorMessage SCHEDULER_ALREADY_REGISTERED = new ErrorMessage("SCHEDULER_ALREADY_REGISTERED", "The Scheduler has already registered in the system with provided name : [{0}].");

    public static final ErrorMessage JOB_EXECUTION_LOG_NOT_FOUND = new ErrorMessage("JOB_EXECUTION_LOG_NOT_FOUND", "System cannot find the job execution log with provided ID : [{0}].");
    //@@formatter:on
}
