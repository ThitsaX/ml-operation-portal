package com.thitsaworks.operation_portal.scheduler;

import com.thitsaworks.operation_portal.hubuser.domain.command.RemoveAnnouncements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AnnouncementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(AnnouncementScheduler.class);


    @Autowired
    private RemoveAnnouncements removeAnnouncements;

    public void runScheduler() {

        RemoveAnnouncements.Output output = this.removeAnnouncements.execute(
                new RemoveAnnouncements.Input());

    }

}
