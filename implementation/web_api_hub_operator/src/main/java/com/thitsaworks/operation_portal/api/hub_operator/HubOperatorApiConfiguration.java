package com.thitsaworks.operation_portal.api.hub_operator;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.CommonUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        HubOperatorUseCaseConfiguration.class, ParticipantUseCaseConfiguration.class, CommonUseCaseConfiguration.class,
        CentralLedgerUseCaseConfiguration.class, WebConfiguration.class, VaultConfiguration.class,
        VaultBasedApplicationSettings.class
})
public class HubOperatorApiConfiguration {

}
