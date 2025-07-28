package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.fail;

public class DeleteSchedulerConfigCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteSchedulerConfigCommandUnitTest.class);

    @Autowired
    private DeleteSchedulerConfigCommand deleteSchedulerConfigCommand;

    @Test
    public void test_deleteSchedulerConfigSuccessfully() throws Exception {
        // Arrange
        Long configId = 1L; // Assuming this ID exists in the test database
        
        // Act
        DeleteSchedulerConfigCommand.Output output = deleteSchedulerConfigCommand.execute(configId);
        
        // Assert
        LOG.info("Scheduler config deleted: {}", output.deleted());
    }
    
    @Test
    public void test_deleteNonExistentSchedulerConfig() {
        // Arrange
        Long nonExistentId = 999L;
        
        // Act & Assert
        try {
            deleteSchedulerConfigCommand.execute(nonExistentId);
            fail("Expected ResourceNotFoundException for non-existent config");
        } catch (ResourceNotFoundException e) {
            LOG.info("Caught expected exception: {}", e.getMessage());
            // Expected exception
        }
    }
}
