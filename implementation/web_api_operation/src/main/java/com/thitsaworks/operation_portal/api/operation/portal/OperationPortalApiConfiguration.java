package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.core.home_message.HomeMessageConfiguration;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.CommonUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.CoreServicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubServicesUseCaseConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        HubOperatorUseCaseConfiguration.class, ParticipantUseCaseConfiguration.class, CommonUseCaseConfiguration.class,
        CentralLedgerUseCaseConfiguration.class, HomeMessageConfiguration.class, WebConfiguration.class,
        VaultConfiguration.class,
        VaultBasedApplicationSettings.class
})
public class OperationPortalApiConfiguration {

}
