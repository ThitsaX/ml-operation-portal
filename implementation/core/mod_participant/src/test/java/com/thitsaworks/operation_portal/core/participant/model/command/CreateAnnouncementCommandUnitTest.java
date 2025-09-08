package com.thitsaworks.operation_portal.core.participant.model.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.command.CreateAnnouncementCommand;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Calendar;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class})
public class CreateAnnouncementCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementCommandUnitTest.class);

    @Autowired
    CreateAnnouncementCommand createAnnouncementCommand;

    @Test
    public void test_createAnnouncementSuccessfully() throws Exception {

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, Calendar.JULY);
        c1.set(Calendar.DATE, 10);
        c1.set(Calendar.YEAR, 2025);

        Instant announcementDate = Instant.ofEpochMilli(c1.getTimeInMillis());

        this.createAnnouncementCommand.execute(
                new CreateAnnouncementCommand.Input("Announcement for January",
                                                    "Announcement for January blah blah blah",
                                                    announcementDate));
    }

}
