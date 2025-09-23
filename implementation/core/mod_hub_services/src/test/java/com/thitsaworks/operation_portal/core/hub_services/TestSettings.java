package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mongo.ReportingMongoConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({VaultSetting.class, VaultConfiguration.class})
public class TestSettings {
    @Bean
    public RedisConfiguration.Settings redisConfigurationSettings(Vault vault) {

        return vault.get(RedisConfiguration.REDIS_SETTINGS_PATH, RedisConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Core.READ_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreDataSourceConfigurationReadDbSettings(Vault vault) {

        return vault.get(CoreDataSourceConfiguration.READ_DB_SETTINGS_PATH,
                CoreDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Core.WRITE_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreDataSourceConfigurationWriteDbSettings(Vault vault) {

        return vault.get(CoreDataSourceConfiguration.WRITE_DB_SETTINGS_PATH,
                CoreDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.READ_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings(Vault vault) {

        return vault.get(HubDataSourceConfiguration.READ_DB_SETTINGS_PATH,
                         HubDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.WRITE_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings(Vault vault) {

        return vault.get(HubDataSourceConfiguration.WRITE_DB_SETTINGS_PATH,
                         HubDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_SETTINGS)
    public ReportingMongoConfiguration.Settings reportingMongoConfigurationReadDbSettings(Vault vault) {

        return vault.get(ReportingMongoConfiguration.READ_SETTINGS_PATH,
                         ReportingMongoConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_SETTINGS)
    public ReportingMongoConfiguration.Settings reportingMongoConfigurationWriteDbSettings(Vault vault) {

        return vault.get(ReportingMongoConfiguration.WRITE_SETTINGS_PATH,
                         ReportingMongoConfiguration.Settings.class);
    }

}

