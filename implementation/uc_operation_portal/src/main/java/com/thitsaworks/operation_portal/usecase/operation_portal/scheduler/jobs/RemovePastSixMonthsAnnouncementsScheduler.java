package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.participant.command.RemoveAnnouncementsCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("RemovePastSixMonthsAnnouncementsScheduler")
public class RemovePastSixMonthsAnnouncementsScheduler
        extends ScheduledJob<SchedulerConfigData, RemoveAnnouncementsCommand.Output> {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsScheduler.class);

    private final RemoveAnnouncementsCommand removeAnnouncementsCommand;

    public RemovePastSixMonthsAnnouncementsScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                                     ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                                     CreateInputAuditCommand createInputAuditCommand,
                                                     CreateOutputAuditCommand createOutputAuditCommand,
                                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                                     ActionAuthorizationManager actionAuthorizationManager,
                                                     ObjectMapper objectMapper,
                                                     RemoveAnnouncementsCommand removeAnnouncementsCommand) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand, createInputAuditCommand,
              createOutputAuditCommand, createExceptionAuditCommand, actionAuthorizationManager, objectMapper);
        this.removeAnnouncementsCommand = removeAnnouncementsCommand;
    }

    //  @Scheduled(cron = "0 0 0 * * ?") // this will run daily
    @Override
    protected RemoveAnnouncementsCommand.Output onExecute(SchedulerConfigData schedulerConfigData) {

        return this.removeAnnouncementsCommand.execute(new RemoveAnnouncementsCommand.Input());

    }

}
