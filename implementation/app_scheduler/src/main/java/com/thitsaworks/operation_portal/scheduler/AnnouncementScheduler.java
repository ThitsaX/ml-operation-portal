package com.thitsaworks.operation_portal.scheduler;

import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncementsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AnnouncementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementScheduler.class);


    @Autowired
    private RemoveAnnouncementsCommand removeAnnouncementsCommand;

    public void runScheduler() {

        RemoveAnnouncementsCommand.Output output = this.removeAnnouncementsCommand.execute(
                new RemoveAnnouncementsCommand.Input());

    }

}
