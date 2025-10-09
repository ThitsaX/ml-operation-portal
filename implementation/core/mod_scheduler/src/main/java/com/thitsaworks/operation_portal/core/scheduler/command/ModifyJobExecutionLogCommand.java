package com.thitsaworks.operation_portal.core.scheduler.command;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

import java.time.LocalDateTime;

public interface ModifyJobExecutionLogCommand {

    record Input(JobExecutionLogId jobExecutionLogId, JobStatus jobStatus, String executionMessage, LocalDateTime endTime) {}

    record Output(boolean updated) {}

    Output execute(Input input) throws DomainException;

}
