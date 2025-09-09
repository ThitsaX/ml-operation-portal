package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.command.ScheduleTaskCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulerConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class ScheduleTaskCommandUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleTaskCommandUnitTest.class);

    @Autowired
    private ScheduleTaskCommand scheduleTaskCommand;

    @Test
    public void test_scheduleTaskSuccessfully() throws Exception {
        // Arrange
        String taskName = "testScheduledTask";
        String cronExpression = "0 0/5 * * * ?"; // Every 5 minutes
        
        // Act
        ScheduleTaskCommand.Output output = scheduleTaskCommand.execute(taskName, cronExpression);
        
        // Assert
        LOG.info("Task scheduling result: {}", output.scheduled());
        
        // Cleanup - cancel the task after test
        try {
            // If there's a way to cancel the task, do it here
            // e.g., cancelScheduledTaskCommand.execute(taskName);
        } catch (Exception e) {
            LOG.warn("Failed to clean up test task: {}", e.getMessage());
        }
    }

    @Test
    public void test_scheduleTaskWithInvalidCron() {
        // Arrange
        String taskName = "invalidCronTask";
        String invalidCron = "invalid-cron-expression";
        
        // Act & Assert
        try {
            scheduleTaskCommand.execute(taskName, invalidCron);
            fail("Expected IllegalArgumentException for invalid cron expression");
        } catch (IllegalArgumentException e) {
            LOG.info("Caught expected exception: {}", e.getMessage());
            // Expected exception
        }
    }
}
