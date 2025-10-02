package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("SchedulingTester")
public class SchedulingTester extends ScheduledJob {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingTester.class);

    public SchedulingTester(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                            ModifyJobExecutionLogCommand modifyJobExecutionLogCommand) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand);
    }

    @Override
    public void onExecute(SchedulerConfigData schedulerConfigData) throws DomainException, InterruptedException {

        LOG.info("Running SchedulingTester job: [{}]", schedulerConfigData);

        Thread.sleep(10000);

        LOG.info("SchedulingTester job:[{}] completed", schedulerConfigData);

    }

}
