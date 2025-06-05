package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.usecase.common.LoginUserAccount;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CommonUseCaseConfiguration.class, MySqlDbSettings.class})
public class LoginUserAccountUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountUnitTest.class);

    @Autowired
    private LoginUserAccount loginUserAccount;

    @Test
    public void test_loginUserAccountSuccessfully() throws DFSPPortalException {

        this.loginUserAccount.execute(new LoginUserAccount.Input(
                new Email("nyeinnyeinei22@gmail.com"), "Nne@12345"));

    }

}
