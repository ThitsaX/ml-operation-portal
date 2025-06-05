package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.usecase.common.ResetCurrentPassword;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class ResetCurrentPasswordUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentPasswordUnitTest.class);

    @Autowired
    private ResetCurrentPassword resetCurrentPassword;

    @Test
    public void test_resetCurrentPasswordSuccessfully() throws Exception {

        this.resetCurrentPassword.execute(new ResetCurrentPassword.Input(
                new Email("nne@gmail.com"), "Nne@12345"));

    }

}
