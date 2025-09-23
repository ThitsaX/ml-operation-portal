package com.thitsaworks.operation_portal.component.infra.mysql.hub;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Import(value = {HubDataSourceConfiguration.class})
public class HubJdbcPersistenceConfiguration {

    @Primary
    @Bean(name = PersistenceQualifiers.Hub.WRITE_JDBC_TEMPLATE)
    public JdbcTemplate writeJdbcTemplate(@Qualifier(PersistenceQualifiers.Hub.WRITE_DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    @Bean(name = PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE)
    public JdbcTemplate readJdbcTemplate(
            @Qualifier(PersistenceQualifiers.Hub.READ_DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }



}
