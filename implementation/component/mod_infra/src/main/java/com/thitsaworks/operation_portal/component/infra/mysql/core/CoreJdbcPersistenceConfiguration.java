package com.thitsaworks.operation_portal.component.infra.mysql.core;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Import(value = {CoreDataSourceConfiguration.class})
public class CoreJdbcPersistenceConfiguration {

    @Primary
    @Bean(name = PersistenceQualifiers.Core.WRITE_JDBC_TEMPLATE)
    public JdbcTemplate writeJdbcTemplate(@Qualifier(PersistenceQualifiers.Core.WRITE_DATA_SOURCE)
                                          DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    @Bean(name = PersistenceQualifiers.Core.READ_JDBC_TEMPLATE)
    public JdbcTemplate readJdbcTemplate(
        @Qualifier(PersistenceQualifiers.Core.READ_DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

}
