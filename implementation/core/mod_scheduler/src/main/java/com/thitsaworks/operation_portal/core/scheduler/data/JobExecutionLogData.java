package com.thitsaworks.operation_portal.core.scheduler.data;

import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;

import java.time.LocalDateTime;

/**
 * Data transfer object for JobExecutionLog entities.
 * Provides a clean interface for transferring job execution log data between layers.
 */
public record JobExecutionLogData(
        Long id,
        String jobName,
        String status,
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
                log.getId(),
                log.getJobName(),
                log.getStatus(),
                log.getStartTime(),
                log.getEndTime(),
                log.getExecutionMessage()
        );
    }
}
