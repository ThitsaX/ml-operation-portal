package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public abstract class ScheduledJob {

    private static final ThreadLocal<JobExecutionLogId> jobExecutionLogId = new InheritableThreadLocal<>();

    private final CreateJobExecutionLogCommand createJobExecutionLogCommand;

    private final ModifyJobExecutionLogCommand modifyJobExecutionLogCommand;

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJob.class);

    void run(SchedulerConfigData schedulerConfigData) throws DomainException {

        try {

            this.beforeExecute(schedulerConfigData);

            this.onExecute(schedulerConfigData);

            this.afterExecute(schedulerConfigData);

        } catch (Exception exception) {

            LOG.info("Scheduler Job: [{}] encountered expression: [{}]",
                     schedulerConfigData.name(), exception.getMessage());

            var jobExecutionLogId = ScheduledJob.jobExecutionLogId.get();

            if (jobExecutionLogId != null) {
                this.modifyJobExecutionLogCommand.execute(new ModifyJobExecutionLogCommand.Input(jobExecutionLogId,
                                                                                                 JobStatus.FAILED,
                                                                                                 exception.getMessage(),
                                                                                                 LocalDateTime.now()));
            }

        }

    }

    protected void beforeExecute(SchedulerConfigData schedulerConfigData) throws DomainException {

        LocalDateTime startTime = LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId())).withNano(0);

        LOG.info("Scheduler Job: [{}] initiated at: [{} ({})]",
                 schedulerConfigData.name(), startTime, schedulerConfigData.zoneId());

        var jobExecutionLogOutput = this.createJobExecutionLogCommand.execute(new CreateJobExecutionLogCommand.Input(
                schedulerConfigData.name(),
                JobStatus.STARTED,
                startTime));

        ScheduledJob.jobExecutionLogId.set(jobExecutionLogOutput.jobExecutionLogId());

    }

    protected void afterExecute(SchedulerConfigData schedulerConfigData) throws DomainException {

        LocalDateTime endTime = LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId())).withNano(0);

        LOG.info("Scheduler Job: [{}] executed successfully at: [{} ({})]",
                 schedulerConfigData.name(), endTime, schedulerConfigData.zoneId());

        JobExecutionLogId jobExecutionLogId = ScheduledJob.jobExecutionLogId.get();

        String jobExecutionMessage = String.format("Job [%s] executed successfully at [%s (%s)]",
                                                   schedulerConfigData.name(),
                                                   endTime, schedulerConfigData.zoneId());

        this.modifyJobExecutionLogCommand.execute(new ModifyJobExecutionLogCommand.Input(jobExecutionLogId,
                                                                                         JobStatus.COMPLETED,
                                                                                         jobExecutionMessage,
                                                                                         endTime));

    }

    protected abstract void onExecute(SchedulerConfigData schedulerConfigData)
            throws DomainException, InterruptedException;

}
