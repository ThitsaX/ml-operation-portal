package com.thitsaworks.dfsp_portal.hubuser.domain.query;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.dfsp_portal.hubuser.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.hubuser.exception.AnnouncementNotFoundException;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.dfsp_portal.hubuser.query.GetAnnouncement;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
@ContextConfiguration(classes = {HubUserConfiguration.class, MySqlDbSettings.class})
public class GetAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementUnitTest.class);

    @Autowired
    private GetAnnouncement getAnnouncement;

    @Test
    public void test_getAnnouncementSuccessfully() throws AnnouncementNotFoundException {

        GetAnnouncement.Output output =
                this.getAnnouncement.execute(new GetAnnouncement.Input(new AnnouncementId(410480760326225920L)));

        if (output != null) {
            LOG.info(output.getAnnouncementId().getId() + ", " + output.getAnnouncementTitle().toString() + ", " +
                    output.getAnnouncementDetail() + ", " + output.getAnnouncementDate());
        } else {
            LOG.info("No record");
        }
    }

}
