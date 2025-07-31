package com.thitsaworks.operation_portal.core.test_iam.IT.engine;

import com.thitsaworks.operation_portal.core.test_iam.IAMTestConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.IT.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.test_iam.IT.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {IAMTestConfiguration.class, TestSettings.class})
public class IAMEngineHandlerIT extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(IAMEngineHandlerIT.class);

}
