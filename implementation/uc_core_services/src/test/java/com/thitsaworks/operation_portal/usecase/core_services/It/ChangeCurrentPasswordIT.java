package com.thitsaworks.operation_portal.usecase.core_services.It;

import com.thitsaworks.operation_portal.usecase.CoreServicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.core_services.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
            CoreServicesUseCaseConfiguration.class, TestSettings.class})
public class ChangeCurrentPasswordIT {
}
