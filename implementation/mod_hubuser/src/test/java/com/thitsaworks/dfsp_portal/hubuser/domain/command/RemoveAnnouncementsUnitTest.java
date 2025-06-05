package com.thitsaworks.dfsp_portal.hubuser.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.dfsp_portal.hubuser.domain.persistence.MySqlDbSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubUserConfiguration.class, MySqlDbSettings.class})
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
