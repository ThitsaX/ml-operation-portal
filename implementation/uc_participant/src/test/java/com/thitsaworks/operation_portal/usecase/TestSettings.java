package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.Vault;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Core.READ_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreDataSourceConfigurationReadDbSettings() {

        return new CoreDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                             "root",
                                                             "password",
                                                             0,
                                                             5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Core.WRITE_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreDataSourceConfigurationWriteDbSettings() {

        return new CoreDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                             "root",
                                                             "password",
                                                             0,
                                                             5);
    }

    @Bean
    public RedisConfiguration.Settings redisConfigurationSettings() {

        return new RedisConfiguration.Settings("redis://localhost:6379");
    }

}
