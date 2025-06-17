package com.thitsaworks.operation_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncement;
import com.thitsaworks.operation_portal.hubuser.domain.TestSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Calendar;

public class CreateAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementUnitTest.class);

    @Autowired
    private CreateAnnouncement createAnnouncement;

    @Test
    public void test_createAnnouncementSuccessfully() throws Exception {

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, Calendar.JANUARY);
        c1.set(Calendar.DATE, 1);
        c1.set(Calendar.YEAR, 2022);

        Instant announcementDate = Instant.ofEpochMilli(c1.getTimeInMillis());

        this.createAnnouncement.execute(
                new CreateAnnouncement.Input("Announcement for January","Announcement for January blah blah blah",null));
    }

}
