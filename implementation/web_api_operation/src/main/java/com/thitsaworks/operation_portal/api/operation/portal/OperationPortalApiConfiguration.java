package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCaseConfiguration;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
    OperationPortalUseCaseConfiguration.class,
    WebConfiguration.class, VaultConfiguration.class,
    VaultBasedApplicationSettings.class
})
public class OperationPortalApiConfiguration {

}
