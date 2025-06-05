package com.thitsaworks.dfsp_portal.hubuser.domain.query;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.dfsp_portal.hubuser.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.hubuser.query.GetAnnouncements;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubUserConfiguration.class, MySqlDbSettings.class})
public class GetAnnouncementsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementsUnitTest.class);

    @Autowired
    private GetAnnouncements getAnnouncements;

    @Test
    public void test_getAnnouncementsSuccessfully() throws Exception {

        GetAnnouncements.Output output = this.getAnnouncements.execute(new GetAnnouncements.Input());

        if (output.getAnnouncementInfoList() != null && output.getAnnouncementInfoList().size() > 0) {
            for (var obj : output.getAnnouncementInfoList())
                LOG.info(obj.getAnnouncementId().getId() + ", " + obj.getAnnouncementTitle() + ", " +
                        obj.getAnnouncementDetail());
        } else {
            LOG.info("No record");
        }
    }

}
