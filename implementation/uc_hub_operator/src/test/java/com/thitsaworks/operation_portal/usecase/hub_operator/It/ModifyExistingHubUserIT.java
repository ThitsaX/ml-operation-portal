package com.thitsaworks.operation_portal.usecase.hub_operator.It;

import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_operator.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.usecase.hub_operator.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                HubOperatorUseCaseConfiguration.class, TestSettings.class})
public class ModifyExistingHubUserIT extends BaseVaultSetUpTest {
}
