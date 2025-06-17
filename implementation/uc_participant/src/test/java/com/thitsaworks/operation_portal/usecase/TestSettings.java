package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class TestSettings {
    @Bean
    public CorePersistenceConfiguration.PoolSizes poolSizes() {

        return new CorePersistenceConfiguration.PoolSizes(5, 10);
    }

    @Bean
    public CorePersistenceConfiguration.Settings settings() {

        return new CorePersistenceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal", "root", "password");
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.READ_POOL_SIZES)
    public SharedDataSourceConfiguration.PoolSizes sharedDataSourceConfigurationReadDbPoolSizes() {
        return new SharedDataSourceConfiguration.PoolSizes(1, 5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.READ_SETTINGS)
    public SharedDataSourceConfiguration.Settings sharedDataSourceConfigurationReadDbSettings() {
        return new SharedDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal", "root", "password");
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.WRITE_POOL_SIZES)
    public SharedDataSourceConfiguration.PoolSizes sharedDataSourceConfigurationWriteDbPoolSizes() {
        return new SharedDataSourceConfiguration.PoolSizes(1, 5);
    }

    @Bean
    @Qualifier(PersistenceQualifiers.Shared.WRITE_SETTINGS)
    public SharedDataSourceConfiguration.Settings sharedDataSourceConfigurationWriteDbSettings() {
        return new SharedDataSourceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal", "root", "password");
    }

    @Bean
    public HazelcastConfiguration.Settings hazelcastConfigurationSettings(){
        return new HazelcastConfiguration.Settings("Re21", "localhost");
    }

}
