package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.scheduler.data.JobExecutionLogData;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import com.thitsaworks.operation_portal.core.scheduler.query.JobExecutionLogQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of the JobExecutionLogQuery interface.
 * Provides methods to retrieve job execution logs with filtering and sorting.
 */
@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class JobExecutionLogJpaQueryHandler implements JobExecutionLogQuery {

    private final JobExecutionLogRepository jobExecutionLogRepository;

    @Override
    public List<JobExecutionLogData> getJobExecutionLogs(Sort sort) {
        List<JobExecutionLog> logs = sort == null 
                ? jobExecutionLogRepository.findAll()
                : jobExecutionLogRepository.findAll(sort);
                
        return logs.stream()
                .map(JobExecutionLogData::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobExecutionLogData> getJobExecutionLogsByJobName(String jobName, Sort sort) {
        List<JobExecutionLog> logs = sort == null
                ? jobExecutionLogRepository.findByJobName(jobName)
                : jobExecutionLogRepository.findByJobName(jobName, sort);
                
        return logs.stream()
                .map(JobExecutionLogData::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobExecutionLogData> getJobExecutionLogsByStatus(String status, Sort sort) {
        List<JobExecutionLog> logs = sort == null
                ? jobExecutionLogRepository.findByStatus(status)
                : jobExecutionLogRepository.findByStatus(status, sort);
                
        return logs.stream()
                .map(JobExecutionLogData::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobExecutionLogData> getJobExecutionLogsByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Sort sort
    ) {
        List<JobExecutionLog> logs = sort == null
                ? jobExecutionLogRepository.findByStartTimeBetween(startDate, endDate)
                : jobExecutionLogRepository.findByStartTimeBetween(startDate, endDate, sort);
                
        return logs.stream()
                .map(JobExecutionLogData::new)
                .collect(Collectors.toList());
    }

    @Override
    public JobExecutionLogData getJobExecutionLog(Long logId) {
        return findJobExecutionLogById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("JobExecutionLog not found with id: " + logId));
    }

    @Override
    public Optional<JobExecutionLogData> findJobExecutionLogById(Long logId) {
        return jobExecutionLogRepository.findById(logId)
                .map(JobExecutionLogData::new);
    }
}
