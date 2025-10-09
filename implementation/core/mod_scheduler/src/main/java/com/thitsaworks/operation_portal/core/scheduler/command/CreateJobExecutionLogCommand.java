package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

import java.time.LocalDateTime;

public interface CreateJobExecutionLogCommand {

    record Input(String jobName, JobStatus jobStatus, LocalDateTime startTime) {}

    record Output(boolean created, JobExecutionLogId jobExecutionLogId) {}

    Output execute(Input input) throws DomainException;

}
