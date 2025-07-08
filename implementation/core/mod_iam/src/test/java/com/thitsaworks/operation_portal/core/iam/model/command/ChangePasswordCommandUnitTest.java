package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.iam.command.ChangePasswordCommand;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class ChangePasswordCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordCommandUnitTest.class);

    @Autowired
    private ChangePasswordCommand changePasswordCommand;

    @Test
    public void test_ChangePasswordSuccessfully() throws DomainException {

        this.changePasswordCommand.execute(
                new ChangePasswordCommand.Input(new PrincipalId(Long.parseLong("392628367895068672")), "testpassword",
                                                "Nne@12345"));
    }

    @Test //expected = PasswordAuthenticationFailureException.class
    public void test_PasswordAuthenticationFailureException()
            throws DomainException {

        this.changePasswordCommand.execute(
                new ChangePasswordCommand.Input(new PrincipalId(Long.parseLong("343028466997653504")), "testpassword",
                                                "newpassword"));
    }

}
