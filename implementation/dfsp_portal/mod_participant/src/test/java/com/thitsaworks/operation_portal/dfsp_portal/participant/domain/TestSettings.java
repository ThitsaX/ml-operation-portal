package com.thitsaworks.operation_portal.dfsp_portal.participant.domain;

import com.thitsaworks.operation_portal.component.infra.mysql.DfspPortalPersistenceConfiguration;
import org.springframework.context.annotation.Bean;

public class TestSettings {
    @Bean
    public DfspPortalPersistenceConfiguration.PoolSizes poolSizes() {

        return new DfspPortalPersistenceConfiguration.PoolSizes(5, 10);
    }

    @Bean
    public DfspPortalPersistenceConfiguration.Settings settings() {

        return new DfspPortalPersistenceConfiguration.Settings("jdbc:mysql://localhost:3306/operation_portal", "root", "password");
    }

}
