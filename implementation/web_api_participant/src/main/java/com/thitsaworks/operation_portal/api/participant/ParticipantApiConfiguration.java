package com.thitsaworks.operation_portal.api.participant;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.core_servicesUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        ParticipantUseCaseConfiguration.class, HubOperatorUseCaseConfiguration.class,
        CentralLedgerUseCaseConfiguration.class,
        CommonUseCaseConfiguration.class, WebConfiguration.class, VaultConfiguration.class,
        VaultBasedApplicationSettings.class})
public class ParticipantApiConfiguration {

}
