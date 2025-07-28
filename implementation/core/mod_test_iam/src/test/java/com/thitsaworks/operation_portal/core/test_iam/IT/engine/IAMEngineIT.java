package com.thitsaworks.operation_portal.core.test_iam.IT.engine;

import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {IAMTestConfiguration.class, TestSettings.class})
public class IAMEngineIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(IAMEngineIT.class);

    @Autowired
    private IAMEngine iamEngine;

    @Test
    public void getActionsByRole() throws IAMException {

        LOG.info("Actions by Role : [{}]", this.iamEngine.getActionsByRole("ADMIN"));
    }
}
