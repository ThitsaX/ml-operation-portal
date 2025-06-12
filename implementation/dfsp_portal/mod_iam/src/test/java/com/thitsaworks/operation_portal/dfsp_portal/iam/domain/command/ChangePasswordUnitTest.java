package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class ChangePasswordUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordUnitTest.class);

    @Autowired
    private ChangePassword changePassword;

    @Test
    public void test_ChangePasswordSuccessfully() throws PasswordAuthenticationFailureException,
                                                         PrincipalNotFoundException {

        this.changePassword.execute(
                new ChangePassword.Input(new PrincipalId(Long.parseLong("392628367895068672")), "testpassword",
                                         "Nne@12345"));
    }

    @Test //expected = PasswordAuthenticationFailureException.class
    public void test_PasswordAuthenticationFailureException()
            throws PasswordAuthenticationFailureException, PrincipalNotFoundException {

        this.changePassword.execute(
                new ChangePassword.Input(new PrincipalId(Long.parseLong("343028466997653504")), "testpassword",
                                         "newpassword"));
    }

}
