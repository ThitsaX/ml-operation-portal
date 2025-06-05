package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.usecase.common.ChangeCurrentPassword;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class ChangeCurrentPasswordUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordUnitTest.class);

    @Autowired
    private ChangeCurrentPassword changeCurrentPassword;

    @Test
    public void test_changeCurrentPasswordSuccessfully() throws Exception {

        this.changeCurrentPassword.execute(new ChangeCurrentPassword.Input(
                new PrincipalId(406044560775536640L), "aye@123",
                "Nne@12345"));

    }

}
