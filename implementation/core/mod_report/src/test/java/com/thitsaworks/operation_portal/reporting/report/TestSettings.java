package com.thitsaworks.operation_portal.reporting.report;

import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.READ_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings() {

        return new HubDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                       "root",
                                                       "password",
                                                       1,
                                                       5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Hub.WRITE_SETTINGS)
    public HubDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings() {

        return new HubDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                       "root",
                                                       "password",
                                                       10,
                                                       20);
    }

}
