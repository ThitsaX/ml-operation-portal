package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Calendar;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementUnitTest.class);

    @Autowired
    private ModifyExistingAnnouncement modifyExistingAnnouncement;

    @Test
    public void test_modifyExistingAnnouncementSuccessfully() throws Exception {

        UseCaseContext.set(new SecurityContext("392628367895068672", "402814821686345728"));

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.MONTH, Calendar.MARCH);
        c1.set(Calendar.DATE, 10);
        c1.set(Calendar.YEAR, 2023);

        Instant announcementDate = Instant.ofEpochMilli(c1.getTimeInMillis());

        this.modifyExistingAnnouncement.execute(
                new ModifyExistingAnnouncement.Input(new AnnouncementId(410480760326225920L), "February Announcement",
                        "Announcement for February blah blah blah",null,
                         false));

    }

}
