package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.PrincipalStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {IAMConfiguration.class})
public class ModifyPrincipalUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyPrincipalUnitTest.class);

    @Autowired
    private ModifyPrincipal modifyPrincipal;

    @Test
    public void test_modifyPrincipalSuccessfully() throws PrincipalNotFoundException {

        this.modifyPrincipal.execute(new ModifyPrincipal.Input(new PrincipalId(1L), PrincipalStatus.INACTIVE));
    }

}
