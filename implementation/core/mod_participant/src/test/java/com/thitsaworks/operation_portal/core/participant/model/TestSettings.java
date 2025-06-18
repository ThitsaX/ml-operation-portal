package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
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
    public HazelcastConfiguration.Settings hazelcastConfigurationSettings(){
        return new HazelcastConfiguration.Settings("Re21", "localhost");
    }

}
