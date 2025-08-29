package com.thitsaworks.operation_portal.api.operation.portal;

import com.thitsaworks.operation_portal.component.infra.mongo.ReportingMongoConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class VaultBasedApplicationSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(VaultBasedApplicationSettings.class);

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
    @Qualifier(PersistenceQualifiers.Reporting.READ_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings(Vault vault) {

        return vault.get(ReportingDataSourceConfiguration.READ_DB_SETTINGS_PATH,
                         ReportingDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.WRITE_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings(Vault vault) {

        return vault.get(ReportingDataSourceConfiguration.WRITE_DB_SETTINGS_PATH,
                         ReportingDataSourceConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.MONGO_READ_SETTINGS)
    public ReportingMongoConfiguration.Settings reportingMongConfigurationReadDbSettings(Vault vault) {

        return vault.get(ReportingMongoConfiguration.READ_SETTINGS_PATH, ReportingMongoConfiguration.Settings.class);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.MONGO_WRITE_SETTINGS)
    public ReportingMongoConfiguration.Settings reportingMongoConfigurationWriteDbSettings(Vault vault) {

        return vault.get(ReportingMongoConfiguration.WRITE_SETTINGS_PATH, ReportingMongoConfiguration.Settings.class);
    }

    @Bean
    public HubServicesConfiguration.Settings hubServiceConfigurationSettings(Vault vault) {

        return vault.get(HubServicesConfiguration.HUB_SERVICE_SETTINGS_PATH, HubServicesConfiguration.Settings.class);
    }

}
