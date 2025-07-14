package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import org.springframework.context.annotation.Bean;

public class VaultSetting {
    @Bean
    public VaultConfiguration.Settings vaultSettings() {

        return VaultConfiguration.Settings.withPropertyOrEnv();

    }
}
