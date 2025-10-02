package com.thitsaworks.operation_portal.core.scheduler.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link JobExecutionLog} entities providing data access operations.
 */
@Repository
public interface JobExecutionLogRepository extends JpaRepository<JobExecutionLog, JobExecutionLogId>,
                                                   QuerydslPredicateExecutor<JobExecutionLog> {
    
    /**
     * Find the top 5 most recent job execution logs, ordered by start time descending.
     *
     * @return a list of the 5 most recent job execution logs
     */
    List<JobExecutionLog> findTop5ByOrderByStartTimeDesc();
    
    /**
     * Find all job execution logs for a specific job name.
     *
     * @param jobName the name of the job to find logs for
     * @return a list of job execution logs for the specified job
     */
    List<JobExecutionLog> findByJobName(String jobName);
    
    /**
     * Find all job execution logs for a specific job name with sorting.
     *
     * @param jobName the name of the job to find logs for
     * @param sort the sort specification
     * @return a sorted list of job execution logs for the specified job
     */
    List<JobExecutionLog> findByJobName(String jobName, Sort sort);
    
    /**
     * Find all job execution logs with a specific jobStatus.
     *
     * @param status the jobStatus to filter by
     * @return a list of job execution logs with the specified jobStatus
     */
    List<JobExecutionLog> findByJobStatus(JobStatus jobStatus);
    
    /**
     * Find all job execution logs with a specific jobStatus with sorting.
     *
     * @param status the jobStatus to filter by
     * @param sort the sort specification
     * @return a sorted list of job execution logs with the specified jobStatus
     */
    List<JobExecutionLog> findByJobStatus(JobStatus jobStatus, Sort sort);
    
    /**
     * Find all job execution logs within a specific date range.
     *
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return a list of job execution logs within the specified date range
     */
    List<JobExecutionLog> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find all job execution logs within a specific date range with sorting.
     *
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @param sort the sort specification
     * @return a sorted list of job execution logs within the specified date range
     */
    List<JobExecutionLog> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);
}
