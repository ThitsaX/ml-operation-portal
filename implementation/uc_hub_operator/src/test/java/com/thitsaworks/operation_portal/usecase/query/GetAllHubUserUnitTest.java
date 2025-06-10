package com.thitsaworks.operation_portal.usecase.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllHubUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetAllHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllHubUserUnitTest.class);

    @Autowired
    private GetAllHubUser getAllHubUser;

    @Test
    public void test_getAllHubUsersSuccessfully() throws Exception {

        GetAllHubUser.Output output = this.getAllHubUser.execute(new GetAllHubUser.Input());

        if (output.getUserInfoList() != null && output.getUserInfoList().size() > 0) {
            for (var obj : output.getUserInfoList())
                LOG.info(obj.getHubUserId().getId() + ", " + obj.getName().toString());
        } else {
            LOG.info("No record");
        }

    }

}
