package com.thitsaworks.operation_portal.usecase.operation_portal.It;

import com.thitsaworks.operation_portal.usecase.OperationPortalUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.operation_portal.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
            OperationPortalUseCaseConfiguration.class, TestSettings.class})
public class ChangeCurrentPasswordIT {
}
