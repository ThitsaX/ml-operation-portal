package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateOrUpdateSchedulerConfigCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateOrUpdateSchedulerConfigCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrUpdateSchedulerConfigCommandUnitTest.class);

    @Autowired
    private CreateOrUpdateSchedulerConfigCommand createOrUpdateSchedulerConfigCommand;

    @Test
    public void test_createSchedulerConfigSuccessfully() throws Exception {
        // Arrange
        String taskName = "testScheduledTask";
        String cronExpression = "0 0/5 * * * ?";
        String description = "Test scheduled task";
        boolean active = true;
        
        CreateOrUpdateSchedulerConfigCommand.Input input = new CreateOrUpdateSchedulerConfigCommand.Input(
            taskName, cronExpression, description, active
        );
        
        // Act
        CreateOrUpdateSchedulerConfigCommand.Output output = 
            createOrUpdateSchedulerConfigCommand.execute(input);
        
        // Assert
        LOG.info("Scheduler config created: {}, cron: {}", 
                output.created(), output.cronExpression());
    }
    
    @Test
    public void test_updateSchedulerConfigSuccessfully() throws Exception {
        // Arrange
        String taskName = "existingScheduledTask";
        String newCronExpression = "0 0/10 * * * ?";
        String updatedDescription = "Updated test scheduled task";
        boolean active = false;
        
        CreateOrUpdateSchedulerConfigCommand.Input input = new CreateOrUpdateSchedulerConfigCommand.Input(
            taskName, newCronExpression, updatedDescription, active
        );
        
        // Act
        CreateOrUpdateSchedulerConfigCommand.Output output = 
            createOrUpdateSchedulerConfigCommand.execute(input);
        
        // Assert
        LOG.info("Scheduler config updated: {}, new cron: {}", 
                output.created(), output.cronExpression());
    }
}
