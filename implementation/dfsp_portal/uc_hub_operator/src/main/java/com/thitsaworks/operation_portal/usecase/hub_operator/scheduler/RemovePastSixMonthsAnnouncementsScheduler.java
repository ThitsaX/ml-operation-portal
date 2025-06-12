package com.thitsaworks.operation_portal.usecase.hub_operator.scheduler;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.domain.command.RemoveAnnouncements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemovePastSixMonthsAnnouncementsScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsScheduler.class);

    @Autowired
    private RemoveAnnouncements removeAnnouncements;

    @DfspWriteTransactional
    @Scheduled(cron = "0 0 0 * * ?") // this will run daily
    public void updateExpireSavingPlan() {

        this.removeAnnouncements.execute(new RemoveAnnouncements.Input());

    }

}
