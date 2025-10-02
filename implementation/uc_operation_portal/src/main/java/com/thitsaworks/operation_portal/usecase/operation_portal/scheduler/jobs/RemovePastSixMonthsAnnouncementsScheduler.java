package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.thitsaworks.operation_portal.core.participant.command.RemoveAnnouncementsCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("RemovePastSixMonthsAnnouncementsScheduler")
public class RemovePastSixMonthsAnnouncementsScheduler extends ScheduledJob {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsScheduler.class);

    private final RemoveAnnouncementsCommand removeAnnouncementsCommand;

    public RemovePastSixMonthsAnnouncementsScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                                     ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                                     RemoveAnnouncementsCommand removeAnnouncementsCommand) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand);
        this.removeAnnouncementsCommand = removeAnnouncementsCommand;
    }

    //  @Scheduled(cron = "0 0 0 * * ?") // this will run daily
    @Override
    public void onExecute(SchedulerConfigData schedulerConfigData) {

        this.removeAnnouncementsCommand.execute(new RemoveAnnouncementsCommand.Input());

    }

}
