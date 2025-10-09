package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulerConfiguration.class, TestSettings.class})
public class CreateJobExecutionLogCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateJobExecutionLogCommandUnitTest.class);

    @Autowired
    private CreateJobExecutionLogCommand createJobExecutionLogCommand;

    @Test
    public void test_createJobExecutionLogSuccessfully() throws Exception {

        CreateJobExecutionLogCommand.Output output =
                createJobExecutionLogCommand.execute(new CreateJobExecutionLogCommand.Input("DailyTransaction",
                                                                                            JobStatus.STARTED,
                                                                                            LocalDateTime.now()));

        // Assert
        LOG.info("Created log : {}", output);
    }

}
