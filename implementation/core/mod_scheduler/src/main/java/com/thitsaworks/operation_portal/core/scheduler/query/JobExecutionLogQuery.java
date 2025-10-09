package com.thitsaworks.operation_portal.core.scheduler.query;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.JobExecutionLogData;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining query operations for job execution logs.
 */
public interface JobExecutionLogQuery {

    /**
     * Get all job execution logs with optional sorting.
     *
     * @param sort the sort specification (can be null for no sorting)
     * @return list of job execution logs
     */
    List<JobExecutionLogData> getJobExecutionLogs(Sort sort);

    /**
     * Get job execution logs filtered by job name with optional sorting.
     *
     * @param jobName the name of the job to filter by
     * @param sort the sort specification (can be null for no sorting)
     * @return list of filtered job execution logs
     */
    List<JobExecutionLogData> getJobExecutionLogsByJobName(String jobName, Sort sort);

    /**
     * Get job execution logs filtered by jobStatus with optional sorting.
     *
     * @param status the jobStatus to filter by
     * @param sort the sort specification (can be null for no sorting)
     * @return list of filtered job execution logs
     */
    List<JobExecutionLogData> getJobExecutionLogsByStatus(JobStatus status, Sort sort);

    /**
     * Get job execution logs within a date range with optional sorting.
     *
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @param sort the sort specification (can be null for no sorting)
     * @return list of filtered job execution logs
     */
    List<JobExecutionLogData> getJobExecutionLogsByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Sort sort
    );

    /**
     * Get a specific job execution log by ID.
     *
     * @param jobExecutionLogId the ID of the log to retrieve
     * @return the job execution log data
     * @throws com.thitsaworks.operation_portal.component.misc.exception.ResourceNotFoundException if log not found
     */
    JobExecutionLogData getJobExecutionLog(JobExecutionLogId jobExecutionLogId) throws DomainException;

    /**
     * Get a specific job execution log by ID if it exists.
     *
     * @param jobExecutionLogId the ID of the log to retrieve
     * @return an Optional containing the job execution log if found, empty otherwise
     */
    Optional<JobExecutionLogData> findJobExecutionLogById(JobExecutionLogId jobExecutionLogId);
}
