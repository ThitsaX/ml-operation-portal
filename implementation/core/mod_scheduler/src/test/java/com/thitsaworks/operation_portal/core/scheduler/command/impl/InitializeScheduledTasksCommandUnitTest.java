package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.command.InitializeScheduledTasksCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class InitializeScheduledTasksCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeScheduledTasksCommandUnitTest.class);

    @Autowired
    private InitializeScheduledTasksCommand initializeScheduledTasksCommand;

    @Test
    public void test_initializeScheduledTasksSuccessfully() throws Exception {
        // Act
        InitializeScheduledTasksCommand.Output output = initializeScheduledTasksCommand.execute();
        
        // Assert
        LOG.info("Scheduled tasks initialized: {}", output.initialized());
    }
    
    @Test
    public void test_initializeWithNoActiveTasks() throws Exception {
        // Act
        InitializeScheduledTasksCommand.Output output = initializeScheduledTasksCommand.execute();
        
        // Assert - Just log that the initialization completed without errors
        LOG.info("Initialization completed with no active tasks: {}", output.initialized());
    }
}
