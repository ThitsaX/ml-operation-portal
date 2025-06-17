package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncements;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoveAnnouncementsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveAnnouncementsUnitTest.class);

    @Autowired
    private RemoveAnnouncements removeAnnouncements;

    @Test
    public void test_removeAnnouncementsSuccessfully() {

        this.removeAnnouncements.execute(
                new RemoveAnnouncements.Input());
    }

}
