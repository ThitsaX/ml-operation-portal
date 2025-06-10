package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Calendar;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class CreateNewAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementUnitTest.class);

    @Autowired
    private CreateNewAnnouncement createNewAnnouncement;

    @Test
    public void test_createNewAnnouncementSuccessfully() throws Exception {

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, Calendar.MARCH);
        c1.set(Calendar.DATE, 10);
        c1.set(Calendar.YEAR, 2023);

        Instant announcementDate = Instant.ofEpochMilli(c1.getTimeInMillis());

        this.createNewAnnouncement.execute(new CreateNewAnnouncement.Input(
                "Announcement for March", "Announcement for March blah blah blah",Instant.ofEpochSecond(1678344536)));

    }

}
