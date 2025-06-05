package com.thitsaworks.dfsp_portal.usecase.query;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.hubuser.identity.HubUserId;
import com.thitsaworks.dfsp_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.GetExistingHubUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetExistingHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingHubUserUnitTest.class);

    @Autowired
    private GetExistingHubUser getExistingHubUser;

    @Test
    public void test_getHubUserSuccessfully() throws Exception {

        GetExistingHubUser.Output output =
                this.getExistingHubUser.execute(new GetExistingHubUser.Input(new HubUserId(410363288277254144L)));

        if (output != null) {
            LOG.info(output.getHubUserId().getId() + ", " + output.getName().toString() + ", " +
                    output.getFirstName().toString() + ", " + output.getLastName().toString());
        } else {
            LOG.info("No record");
        }

    }

}
