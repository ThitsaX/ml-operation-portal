package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.identifier.TraceId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public abstract class ScheduledJob<I, O> {

    private static final ThreadLocal<JobExecutionLogId> jobExecutionLogId = new InheritableThreadLocal<>();

    private static final ThreadLocal<AuditId> auditId = new InheritableThreadLocal<>();

    private final CreateJobExecutionLogCommand createJobExecutionLogCommand;

    private final ModifyJobExecutionLogCommand modifyJobExecutionLogCommand;

    private final CreateInputAuditCommand createInputAuditCommand;

    private final CreateOutputAuditCommand createOutputAuditCommand;

    private final CreateExceptionAuditCommand createExceptionAuditCommand;

    private final ActionAuthorizationManager actionAuthorizationManager;

    private final ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJob.class);

    void run(SchedulerConfigData schedulerConfigData) {

        try {

            MDC.put("TRACE_ID",
                    String.valueOf(Snowflake.get()
                                            .nextId()));
            this.beforeExecute(schedulerConfigData);

            O output = this.onExecute(schedulerConfigData);

            this.afterExecute(schedulerConfigData, output);

        } catch (Exception exception) {

            LOG.info("Scheduler Job: [{}] encountered exception: [{}]",
                     schedulerConfigData.name(), exception.getMessage());

            try {

                JobExecutionLogId jobExecutionLogId = ScheduledJob.jobExecutionLogId.get();

                if (jobExecutionLogId != null) {
                    this.modifyJobExecutionLogCommand.execute(new ModifyJobExecutionLogCommand.Input(jobExecutionLogId,
                                                                                                     JobStatus.FAILED,
                                                                                                     exception.getMessage(),
                                                                                                     LocalDateTime.now()));
                }

                AuditId auditId = ScheduledJob.auditId.get();

                if (auditId != null) {
                    this.createExceptionAuditCommand.execute(new CreateExceptionAuditCommand.Input(auditId,
                                                                                                   exception.getMessage()));
                }

            } catch (AuditException e) {

                LOG.info("Audit Exception: [{}]",
                         e.getErrorMessage()
                          .getDefaultMessage());

            } catch (DomainException e) {

                LOG.info("JobExecutionLog Exception: [{}]",
                         e.getErrorMessage()
                          .getDefaultMessage());

            }

        } finally {

            MDC.clear();
        }

    }

    protected void beforeExecute(SchedulerConfigData schedulerConfigData)
        throws DomainException, JsonProcessingException {

        LocalDateTime
            startTime =
            LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId()))
                         .withNano(0);

        LOG.info("Scheduler Job: [{}] initiated at: [{} ({})]",
                 schedulerConfigData.name(), startTime, schedulerConfigData.zoneId());

        ScheduledJob.jobExecutionLogId.set(this.createJobExecutionLogCommand.execute(new CreateJobExecutionLogCommand.Input(
                                                   schedulerConfigData.name(),
                                                   JobStatus.STARTED,
                                                   startTime))
                                                                            .jobExecutionLogId());

        var action = this.actionAuthorizationManager.getAction(new ActionCode(schedulerConfigData.jobName()));

        String inputJson, inputInfo;
        try {

            inputJson = this.objectMapper.writeValueAsString(schedulerConfigData);
            inputInfo = MaskPassword.maskPassword(this.objectMapper, inputJson);

        } catch (Exception e) {
            throw new SystemException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }

        ScheduledJob.auditId.set(
            this.createInputAuditCommand.execute(new CreateInputAuditCommand.Input(action.actionId(),
                                                                                   null,
                                                                                   null,
                                                                                   new TraceId(Long.valueOf(MDC.get(
                                                                                       "TRACE_ID"))),
                                                                                   inputInfo))
                                        .auditId());

    }

    protected void afterExecute(SchedulerConfigData schedulerConfigData, O output)
        throws DomainException {

        LocalDateTime
            endTime =
            LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId()))
                         .withNano(0);

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

        String outputJson, outputInfo;

        try {

            outputJson = this.objectMapper.writeValueAsString(output);
            outputInfo = MaskPassword.maskPassword(this.objectMapper, outputJson);

        } catch (Exception e) {

            throw new SystemException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }

        AuditId auditId = ScheduledJob.auditId.get();

        if (auditId != null) {

            this.createOutputAuditCommand.execute(new CreateOutputAuditCommand.Input(auditId, outputInfo));

        }

    }

    protected abstract O onExecute(SchedulerConfigData schedulerConfigData)
        throws DomainException, InterruptedException;

}
