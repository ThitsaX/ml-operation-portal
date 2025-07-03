package com.thitsaworks.operation_portal.core.audit.model;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.READ_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                             "root",
                                                             "password",
                                                             0,
                                                             5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.WRITE_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
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
