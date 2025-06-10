package com.thitsaworks.operation_portal.usecase.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllAnnouncement;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetAllAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementUnitTest.class);

    @Autowired
    private GetAllAnnouncement getAllAnnouncement;

    @Test
    public void test_getAllAnnouncementsSuccessfully() throws Exception {

        GetAllAnnouncement.Output output = this.getAllAnnouncement.execute(new GetAllAnnouncement.Input());

        if (output.getAnnouncementInfoList() != null && output.getAnnouncementInfoList().size() > 0) {
            for (var obj : output.getAnnouncementInfoList())
                LOG.info(obj.getAnnouncementId().getId() + ", " + obj.getAnnouncementTitle() + ", " +
                        obj.getAnnouncementDetail());
        } else {
            LOG.info("No record");
        }

    }

}
