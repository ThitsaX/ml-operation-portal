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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component("ReportGeneratorScheduler")
public class ReportGeneratorScheduler extends ScheduledJob<SchedulerConfigData, ReportGeneratorScheduler.Output> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorScheduler.class);

    private static final int MAX_CONCURRENT_REPORT_GENERATIONS = 3;

    private final ReportGenerator reportGenerator;
    private final Executor reportGenerationTaskExecutor;

    public ReportGeneratorScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                    ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                    CreateInputAuditCommand createInputAuditCommand,
                                    CreateOutputAuditCommand createOutputAuditCommand,
                                    CreateExceptionAuditCommand createExceptionAuditCommand,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    ObjectMapper objectMapper,
                                    ReportGenerator reportGenerator,
                                    @Qualifier("reportGenerationTaskExecutor")
                                    Executor reportGenerationTaskExecutor) {

        super(createJobExecutionLogCommand,
              modifyJobExecutionLogCommand,
              createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              actionAuthorizationManager,
              objectMapper);

        this.reportGenerator = reportGenerator;
        this.reportGenerationTaskExecutor = reportGenerationTaskExecutor;
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

        long runningCount = this.reportGenerator.countRunning();
        int availableSlots = (int) Math.max(0, MAX_CONCURRENT_REPORT_GENERATIONS - runningCount);
        int dispatchedCount = 0;

        for (int i = 0; i < availableSlots; i++) {
            try {
                this.reportGenerationTaskExecutor.execute(this.reportGenerator::generateNextPending);
                dispatchedCount++;
            } catch (TaskRejectedException exception) {
                LOG.warn("Report generation task rejected by executor: {}", exception.getMessage());
                break;
            }
        }

        return new Output(dispatchedCount, runningCount);
    }

    public record Output(int dispatchedCount,
                         long runningCount) {

    }
}
