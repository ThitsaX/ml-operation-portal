package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.usecase.HubServicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.CommonUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
    HubOperatorUseCaseConfiguration.class, ParticipantUseCaseConfiguration.class, CommonUseCaseConfiguration.class,
    HubServicesUseCaseConfiguration.class, WebConfiguration.class, VaultConfiguration.class,
    VaultBasedApplicationSettings.class
})
public class OperationPortalApiConfiguration {

}
