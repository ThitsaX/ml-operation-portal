package com.thitsaworks.operation_portal.central_ledger.report;

import com.thitsaworks.operation_portal.component.infra.mysql.SharedDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSettings {

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.READ_POOL_SIZES)
    public SharedDataSourceConfiguration.PoolSizes sharedDataSourceConfigurationReadDbPoolSizes() {
        return new SharedDataSourceConfiguration.PoolSizes(1, 5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.READ_SETTINGS)
    public SharedDataSourceConfiguration.Settings sharedDataSourceConfigurationReadDbSettings() {
        return new SharedDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger", "root", "password");
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.WRITE_POOL_SIZES)
    public SharedDataSourceConfiguration.PoolSizes sharedDataSourceConfigurationWriteDbPoolSizes() {
        return new SharedDataSourceConfiguration.PoolSizes(1, 5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.WRITE_SETTINGS)
    public SharedDataSourceConfiguration.Settings sharedDataSourceConfigurationWriteDbSettings() {
        return new SharedDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/central_ledger", "root", "password");
    }

}
