package com.thitsaworks.operation_portal.hubuser.domain;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.READ_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                             "root",
                                                             "root",
                                                             0,
                                                             5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.WRITE_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                             "root",
                                                             "root",
                                                             0,
                                                             5);
    }

    @Bean
    public RedisConfiguration.Settings redisConfigurationSettings() {

        return new RedisConfiguration.Settings("redis://localhost:6379");

    }

    @Bean
    @Qualifier(PersistenceQualifiers.Core.READ_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreReadDataSource() {

        return new CoreDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                        "root",
                                                        "root",
                                                        0,
                                                        5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Core.WRITE_SETTINGS)
    public CoreDataSourceConfiguration.Settings coreWriteSettings() {

        return new CoreDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal",
                                                        "root",
                                                        "root",
                                                        0,
                                                        5);
    }
}
