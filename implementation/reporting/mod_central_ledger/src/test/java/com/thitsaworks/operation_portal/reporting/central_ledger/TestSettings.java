package com.thitsaworks.operation_portal.reporting.central_ledger;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.READ_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationReadDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                             "root",
                                                             "password",
                                                             0,
                                                             5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Reporting.WRITE_SETTINGS)
    public ReportingDataSourceConfiguration.Settings reportingDataSourceConfigurationWriteDbSettings() {

        return new ReportingDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger",
                                                             "root",
                                                             "password",
                                                             0,
                                                             5);
    }

}
