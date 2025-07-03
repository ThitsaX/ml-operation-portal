package com.thitsaworks.operation_portal.usecase.common.It;

import com.thitsaworks.operation_portal.usecase.CommonUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.common.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                CommonUseCaseConfiguration.class, TestSettings.class})
public class ChangeCurrentPasswordIT {
}
