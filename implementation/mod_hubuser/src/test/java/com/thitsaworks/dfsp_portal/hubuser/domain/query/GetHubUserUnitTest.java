package com.thitsaworks.dfsp_portal.hubuser.domain.query;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.dfsp_portal.hubuser.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.hubuser.identity.HubUserId;
import com.thitsaworks.dfsp_portal.hubuser.query.GetHubUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubUserConfiguration.class, MySqlDbSettings.class})
public class GetHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubUserUnitTest.class);

    @Autowired
    private GetHubUser getHubUser;

    @Test
    public void test_getHubUserSuccessfully() throws Exception {

        GetHubUser.Output output = this.getHubUser.execute(new GetHubUser.Input(new HubUserId(410363288277254144L)));

        if (output != null) {
            LOG.info(output.getHubUserId().getId() + ", " + output.getName().toString() + ", " + output.getFirstName() +
                    ", " + output.getLastName());
        } else {
            LOG.info("No record");
        }
    }

}
