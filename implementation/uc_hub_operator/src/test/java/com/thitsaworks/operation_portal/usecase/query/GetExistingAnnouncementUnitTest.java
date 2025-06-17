package com.thitsaworks.operation_portal.usecase.query;

import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetExistingAnnouncement;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetExistingAnnouncementUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingAnnouncementUnitTest.class);

    @Autowired
    private GetExistingAnnouncement getExistingAnnouncement;

    @Test
    public void test_getExistingAnnouncementSuccessfully() throws DFSPPortalException {

        UseCaseContext.set(new SecurityContext("392628367895068672", "402814821686345728"));

        GetExistingAnnouncement.Output output = this.getExistingAnnouncement.execute(
                new GetExistingAnnouncement.Input(new AnnouncementId(410480760326225920L)));

        if (output != null) {
            LOG.info(output.getAnnouncementId().getId() + ", " + output.getAnnouncementTitle() + ", " +
                    output.getAnnouncementDetail() + ", " + output.getAnnouncementDate().toString());
        } else {
            LOG.info("No record");
        }

    }

}
