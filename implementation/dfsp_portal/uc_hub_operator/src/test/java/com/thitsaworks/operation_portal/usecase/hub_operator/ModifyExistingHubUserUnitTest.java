package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {HubOperatorUseCaseConfiguration.class, MySqlDbSettings.class})
public class ModifyExistingHubUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingHubUserUnitTest.class);

    @Autowired
    private ModifyExistingHubUser modifyExistingHubUser;

    @Test
    public void test_modifyExistingUserSuccessfully() throws Exception {

        this.modifyExistingHubUser.execute(
                new ModifyExistingHubUser.Input(new HubUserId(410363288277254144L), "Ingyin", "Ingyin Thandar",
                        "Khaing", "BA"));

    }

}
