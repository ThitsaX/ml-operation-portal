package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
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
public class DeleteSchedulerConfigCommandUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteSchedulerConfigCommandUnitTest.class);

    @Autowired
    private DeleteSchedulerConfigCommand deleteSchedulerConfigCommand;

    @Test
    public void test_deleteSchedulerConfigSuccessfully() throws Exception {
        // Arrange
        SchedulerConfigId configId = new SchedulerConfigId(1L); // Assuming this ID exists in the test database
        
        // Act
        DeleteSchedulerConfigCommand.Output output = deleteSchedulerConfigCommand.execute(configId);
        
        // Assert
        LOG.info("Scheduler config deleted: {}", output.deleted());
    }
    
    @Test
    public void test_deleteNonExistentSchedulerConfig() {
        // Arrange
        SchedulerConfigId nonExistentId = new SchedulerConfigId(999L);
        
        // Act & Assert
        try {
            deleteSchedulerConfigCommand.execute(nonExistentId);
            fail("Expected ResourceNotFoundException for non-existent config");
        } catch (DomainException e) {
            LOG.info("Caught expected exception: {}", e.getMessage());
            // Expected exception
        }
    }
}
