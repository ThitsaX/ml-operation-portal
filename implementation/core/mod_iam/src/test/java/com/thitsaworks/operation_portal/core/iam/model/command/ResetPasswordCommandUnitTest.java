package com.thitsaworks.operation_portal.core.iam.model.command;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.iam.command.ResetPasswordCommand;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class ResetPasswordCommandUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordCommandUnitTest.class);

    @Autowired
    private ResetPasswordCommand resetPasswordCommand;

    @Test
    public void test_resetPasswordSuccessfully() throws DomainException {

        ResetPasswordCommand.Output output =
                this.resetPasswordCommand.execute(new ResetPasswordCommand.Input(new PrincipalId(392628367895068672L), "Nne@12345"));

        LOG.info("accessKey : {}", output.accessKey().getId());
        LOG.info("secretKey : {}", output.secretKey());
    }

}
