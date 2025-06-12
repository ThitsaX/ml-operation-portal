package com.thitsaworks.operation_portal.hubuser.domain.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.hubuser.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.query.GetHubUsers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubUserConfiguration.class, MySqlDbSettings.class})
public class GetHubUsersUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubUsersUnitTest.class);

    @Autowired
    private GetHubUsers getHubUsers;

    @Test
    public void test_getHubUsersSuccessfully() throws Exception {

        GetHubUsers.Output output = this.getHubUsers.execute(new GetHubUsers.Input());

        if (output.getUserInfoList() != null && output.getUserInfoList().size() > 0) {
            for (var obj : output.getUserInfoList())
                LOG.info(obj.getHubUserId().getId() + ", " + obj.getName().toString());
        } else {
            LOG.info("No record");
        }
    }

}
