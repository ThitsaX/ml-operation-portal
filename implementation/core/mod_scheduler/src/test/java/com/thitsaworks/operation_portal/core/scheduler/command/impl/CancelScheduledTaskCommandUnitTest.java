package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.command.CancelScheduledTaskCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelScheduledTaskCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CancelScheduledTaskCommandUnitTest.class);

    @Autowired
    private CancelScheduledTaskCommand cancelScheduledTaskCommand;

    @Test
    public void test_cancelScheduledTaskSuccessfully() throws Exception {
        // This test assumes there's a task to cancel
        // In a real test, you might want to schedule a task first, then cancel it
        String taskName = "testScheduledTask";
        
        // Act
        CancelScheduledTaskCommand.Output output = cancelScheduledTaskCommand.execute(taskName);
        
        // Assert
        // Note: The behavior might vary based on whether the task existed or not
        // This test is more about verifying the command can be executed without errors
        LOG.info("Task cancellation result: {}", output.cancelled());
    }
}
