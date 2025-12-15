package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("SchedulingTester")
public class SchedulingTester {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingTester.class);

    public SchedulingTester(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                            ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                            CreateInputAuditCommand createInputAuditCommand,
                            CreateOutputAuditCommand createOutputAuditCommand,
                            CreateExceptionAuditCommand createExceptionAuditCommand,
                            ActionAuthorizationManager actionAuthorizationManager,
                            ObjectMapper objectMapper) {

    }

    protected SchedulingTester.Output onExecute(SchedulerConfigData schedulerConfigData)
        throws DomainException, InterruptedException {

        LOG.info("Running SchedulingTester job: [{}]", schedulerConfigData);

        Thread.sleep(10000);

        LOG.info("SchedulingTester job:[{}] completed", schedulerConfigData);

        return new Output(String.format("SchedulingTester job:[%s] completed", schedulerConfigData));

    }

    public record Output(String outputLog) { }

}
