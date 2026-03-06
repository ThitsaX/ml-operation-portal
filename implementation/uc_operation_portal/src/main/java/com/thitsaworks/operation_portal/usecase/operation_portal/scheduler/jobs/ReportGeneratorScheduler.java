package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("ReportGeneratorScheduler")
public class ReportGeneratorScheduler extends ScheduledJob<SchedulerConfigData, ReportGeneratorScheduler.Output> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorScheduler.class);

    private static final int MAX_BATCH_SIZE_PER_RUN = 100;

    private final ReportGenerator reportGenerator;

    public ReportGeneratorScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                    ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                    CreateInputAuditCommand createInputAuditCommand,
                                    CreateOutputAuditCommand createOutputAuditCommand,
                                    CreateExceptionAuditCommand createExceptionAuditCommand,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    ObjectMapper objectMapper,
                                    ReportGenerator reportGenerator) {

        super(createJobExecutionLogCommand,
              modifyJobExecutionLogCommand,
              createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              actionAuthorizationManager,
              objectMapper);

        this.reportGenerator = reportGenerator;
    }

    @Override
    public void run(SchedulerConfigData schedulerConfigData) {

        try {

            this.onExecute(schedulerConfigData);

        } catch (Exception exception) {

            LOG.error("ReportGeneratorScheduler failed: [{}]", exception.getMessage(), exception);
        }
    }

    @Override
    protected Output onExecute(SchedulerConfigData schedulerConfigData) throws DomainException {

        int processedCount = 0;

        while (processedCount < MAX_BATCH_SIZE_PER_RUN && this.reportGenerator.generateNextPending()) {

            processedCount++;
        }

        return new Output(processedCount);
    }

    public record Output(int processedCount) {

    }
}
