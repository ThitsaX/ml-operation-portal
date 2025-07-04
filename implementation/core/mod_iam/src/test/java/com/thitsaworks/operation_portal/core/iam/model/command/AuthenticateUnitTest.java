package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.iam.command.Authenticate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class AuthenticateUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateUnitTest.class);
    @Autowired
    private Authenticate authenticate;

    @Test
    public void test_authenticateSuccessfully()
            throws DomainException {

        this.authenticate.execute(
                new Authenticate.Input(new PrincipalId(Long.parseLong("392628367895068672")), "Nne@12345"));
    }

}
