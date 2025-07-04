package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.iam.command.ResetPassword;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class ResetPasswordUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordUnitTest.class);

    @Autowired
    private ResetPassword resetPassword;

    @Test
    public void test_resetPasswordSuccessfully() throws DomainException {

        ResetPassword.Output output =
                this.resetPassword.execute(new ResetPassword.Input(new PrincipalId(392628367895068672L), "Nne@12345"));

        LOG.info("accessKey : {}", output.accessKey().getId());
        LOG.info("secretKey : {}", output.secretKey());
    }

}
