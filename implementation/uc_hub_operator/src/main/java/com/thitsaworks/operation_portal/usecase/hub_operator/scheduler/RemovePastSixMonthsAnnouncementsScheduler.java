package com.thitsaworks.operation_portal.usecase.hub_operator.scheduler;

import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncements;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePastSixMonthsAnnouncementsScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsScheduler.class);

    private final RemoveAnnouncements removeAnnouncements;

    @Scheduled(cron = "0 0 0 * * ?") // this will run daily
    public void updateExpireSavingPlan() {

        this.removeAnnouncements.execute(new RemoveAnnouncements.Input());

    }

}
