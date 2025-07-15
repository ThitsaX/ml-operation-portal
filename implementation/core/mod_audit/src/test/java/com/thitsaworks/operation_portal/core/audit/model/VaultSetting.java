package com.thitsaworks.operation_portal.core.audit.model;

import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import org.springframework.context.annotation.Bean;

public class VaultSetting {
    @Bean
    public VaultConfiguration.Settings vaultSettings() {

        return VaultConfiguration.Settings.withPropertyOrEnv();

    }
}
