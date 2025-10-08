package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulerConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class CreateSchedulerConfigCommandUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigCommandUnitTest.class);

    @Autowired
    private CreateSchedulerConfigCommand createSchedulerConfigCommand;

    @Test
    public void test_createSchedulerConfigSuccessfully() throws Exception {
        // Arrange
        String configName = "DailyTransaction";
        String taskName = "TransactionScheduler";
        String cronExpression = "0 0/8 * * * ?";
        String description = "Test scheduled task";
        boolean active = true;
        String zoneId = "+06:30";

        CreateSchedulerConfigCommand.Input input = new CreateSchedulerConfigCommand.Input(
                configName, taskName, description, cronExpression, zoneId
        );
        
        // Act
        CreateSchedulerConfigCommand.Output output =
                createSchedulerConfigCommand.execute(input);
        
        // Assert
        LOG.info("Scheduler config name: {}, task name: {}, cron: {}",
                 output.schedulerConfigData().name(),
                 output.schedulerConfigData().jobName(),
                 output.schedulerConfigData().cronExpression());
    }
}
