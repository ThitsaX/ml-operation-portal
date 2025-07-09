package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.usecase.CoreServicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubServicesUseCaseConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
    CoreServicesUseCaseConfiguration.class,
    HubServicesUseCaseConfiguration.class, WebConfiguration.class, VaultConfiguration.class,
    VaultBasedApplicationSettings.class
})
public class OperationPortalApiConfiguration {

}
