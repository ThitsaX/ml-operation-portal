package com.thitsaworks.dfsp_portal.iam.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
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
    public void test_resetPasswordSuccessfully() throws PrincipalNotFoundException {

        ResetPassword.Output output =
                this.resetPassword.execute(new ResetPassword.Input(new PrincipalId(392628367895068672L), "Nne@12345"));

        LOG.info("accessKey : {}", output.getAccessKey().getId());
        LOG.info("secretKey : {}", output.getSecretKey());
    }

}
