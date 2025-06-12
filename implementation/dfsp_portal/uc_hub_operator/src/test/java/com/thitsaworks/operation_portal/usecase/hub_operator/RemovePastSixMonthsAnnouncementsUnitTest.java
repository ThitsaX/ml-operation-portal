package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class RemovePastSixMonthsAnnouncementsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsUnitTest.class);

    @Autowired
    private RemovePastSixMonthsAnnouncements removePastSixMonthsAnnouncements;

    @Test
    public void test_removePastSixMonthsAnnouncementsSuccessfully() throws Exception {

        this.removePastSixMonthsAnnouncements.execute(new RemovePastSixMonthsAnnouncements.Input());

    }

}
