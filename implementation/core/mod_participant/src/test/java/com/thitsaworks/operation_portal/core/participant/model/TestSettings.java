package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class TestSettings {
    @Bean
    @Qualifier(PersistenceQualifiers.Hub.READ_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings() {

        return new HubDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                       "root",
                                                       "password",
                                                       0,
                                                       5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.WRITE_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings() {

        return new HubDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                       "root",
                                                       "password",
                                                       0,
                                                       5);
    }

    @Bean
    public RedisConfiguration.Settings redisConfigurationSettings(Vault vault) {

        return new RedisConfiguration.Settings("redis://localhost:6379");
    }

}
