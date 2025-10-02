package com.thitsaworks.operation_portal.core.scheduler.data;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;

import java.time.LocalDateTime;

/**
 * Data transfer object for JobExecutionLog entities.
 * Provides a clean interface for transferring job execution log data between layers.
 */
public record JobExecutionLogData(
        JobExecutionLogId jobExecutionLogId,
        String jobName,
        JobStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String executionMessage
) {
    /**
     * Creates a new JobExecutionLogData from a JobExecutionLog entity.
     *
     * @param log the JobExecutionLog entity to convert
     */
    public JobExecutionLogData(JobExecutionLog log) {
        this(
                log.getJobExecutionLogId(),
                log.getJobName(),
                log.getJobStatus(),
                log.getStartTime(),
                log.getEndTime(),
                log.getExecutionMessage()
        );
    }
}
