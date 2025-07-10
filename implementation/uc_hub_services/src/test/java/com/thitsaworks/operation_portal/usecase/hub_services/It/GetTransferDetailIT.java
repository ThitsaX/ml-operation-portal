package com.thitsaworks.operation_portal.usecase.hub_services.It;

import com.thitsaworks.operation_portal.usecase.HubServicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.hub_services.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
            HubServicesUseCaseConfiguration.class, TestSettings.class})
public class GetTransferDetailIT {
}
