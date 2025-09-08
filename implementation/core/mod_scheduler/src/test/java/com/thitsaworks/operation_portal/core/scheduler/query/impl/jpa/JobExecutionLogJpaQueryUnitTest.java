package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulerConfiguration.class, TestSettings.class, RedisConfiguration.class})
@Sql(scripts = "/data-test.sql")
@Transactional
public class JobExecutionLogJpaQueryUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(JobExecutionLogJpaQueryUnitTest.class);

    @Autowired
    private JobExecutionLogRepository jobExecutionLogRepository;

    @Autowired
    private JobExecutionLogJpaQueryHandler queryHandler;

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void getJobExecutionLogs_ShouldReturnAllLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogs(Sort.by("startTime"));

        LOG.info("JobExecutionLogs: {}", result);
    }

    @Test
    void getJobExecutionLogsByJobName_ShouldReturnMatchingLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogsByJobName("testJob1", Sort.by("startTime"));

        LOG.info("JobExecutionLog by job name: {}", result);
    }

    @Test
    void getJobExecutionLogsByStatus_ShouldReturnMatchingLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogsByStatus("COMPLETED", Sort.by("startTime"));

        LOG.info("JobExecutionLog by status: {}", result);
    }

    @Test
    void getJobExecutionLogsByDateRange_ShouldReturnLogsInRange() {
        // Arrange
        LocalDateTime startDate = now.minusDays(1);
        LocalDateTime endDate = now.plusDays(1);
        
        // Act
        var result = queryHandler.getJobExecutionLogsByDateRange(
            startDate, endDate, Sort.by("startTime"));
            
        LOG.info("JobExecutionLog by date range: {}", result);
    }

    @Test
    void getJobExecutionLog_ShouldReturnLogWhenFound() {
        // Act
        var result = queryHandler.getJobExecutionLog(1001L);

        assertNotNull(result);
        assertEquals(1001L, result.jobExecutionLogId());
        LOG.info("JobExecutionLog: {}", result);
    }

    @Test
    void getJobExecutionLog_ShouldThrowWhenNotFound() {
        // Act & Assert
        assertThrows(Exception.class, () -> queryHandler.getJobExecutionLog(9999L));
    }

    @Test
    void findJobExecutionLogById_ShouldReturnEmptyWhenNotFound() {
        // Act
        var result = queryHandler.findJobExecutionLogById(9999L);

        // Assert
        assertTrue(result.isEmpty());
    }
}
