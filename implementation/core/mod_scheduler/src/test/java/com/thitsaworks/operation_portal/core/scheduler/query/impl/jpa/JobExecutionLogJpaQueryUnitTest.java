package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JobExecutionLogJpaQueryUnitTest extends EnvAwareUnitTest {

    @Autowired
    private JobExecutionLogRepository jobExecutionLogRepository;

    @Autowired
    private JobExecutionLogJpaQueryHandler queryHandler;

    private JobExecutionLog testLog1;
    private JobExecutionLog testLog2;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Clear existing data
        jobExecutionLogRepository.deleteAll();
        
        // Create and save test data
        testLog1 = new JobExecutionLog();
        testLog1.setJobName("testJob1");
        testLog1.setStatus("COMPLETED");
        testLog1.setStartTime(now.minusHours(1));
        testLog1.setEndTime(now);
        testLog1.setExecutionMessage("Job completed successfully");
        testLog1 = jobExecutionLogRepository.save(testLog1);

        testLog2 = new JobExecutionLog();
        testLog2.setJobName("testJob2");
        testLog2.setStatus("FAILED");
        testLog2.setStartTime(now.minusHours(2));
        testLog2.setEndTime(now.minusHours(1));
        testLog2.setExecutionMessage("Job failed");
        testLog2 = jobExecutionLogRepository.save(testLog2);
        
        // Flush to ensure data is written to the database
        jobExecutionLogRepository.flush();
    }

    @Test
    void getJobExecutionLogs_ShouldReturnAllLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogs(Sort.by("startTime"));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(log -> log.jobName().equals("testJob1")));
        assertTrue(result.stream().anyMatch(log -> log.jobName().equals("testJob2")));
    }

    @Test
    void getJobExecutionLogsByJobName_ShouldReturnMatchingLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogsByJobName("testJob1", Sort.by("startTime"));

        // Assert
        assertEquals(1, result.size());
        assertEquals("testJob1", result.get(0).jobName());
        assertEquals("COMPLETED", result.get(0).status());
    }

    @Test
    void getJobExecutionLogsByStatus_ShouldReturnMatchingLogs() {
        // Act
        var result = queryHandler.getJobExecutionLogsByStatus("COMPLETED", Sort.by("startTime"));

        // Assert
        assertEquals(1, result.size());
        assertEquals("testJob1", result.get(0).jobName());
        assertEquals("COMPLETED", result.get(0).status());
    }

    @Test
    void getJobExecutionLogsByDateRange_ShouldReturnLogsInRange() {
        // Arrange
        LocalDateTime startDate = now.minusDays(1);
        LocalDateTime endDate = now.plusDays(1);
        
        // Act
        var result = queryHandler.getJobExecutionLogsByDateRange(
            startDate, endDate, Sort.by("startTime"));
            
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(log -> log.jobName().equals("testJob1")));
        assertTrue(result.stream().anyMatch(log -> log.jobName().equals("testJob2")));
    }

    @Test
    void getJobExecutionLog_ShouldReturnLogWhenFound() {
        // Act
        var result = queryHandler.getJobExecutionLog(testLog1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testLog1.getId(), result.id());
        assertEquals("testJob1", result.jobName());
        assertEquals("COMPLETED", result.status());
    }

    @Test
    void getJobExecutionLog_ShouldThrowWhenNotFound() {
        // Act & Assert
        assertThrows(Exception.class, () -> queryHandler.getJobExecutionLog(999L));
    }

    @Test
    void findJobExecutionLogById_ShouldReturnEmptyWhenNotFound() {
        // Act
        var result = queryHandler.findJobExecutionLogById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }
}
