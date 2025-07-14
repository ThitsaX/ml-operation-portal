package com.thitsaworks.operation_portal.component.infra.mysql.reporting;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Import(value = {ReportingDataSourceConfiguration.class})
public class ReportingJdbcPersistenceConfiguration {

    @Primary
    @Bean(name = PersistenceQualifiers.Reporting.WRITE_JDBC_TEMPLATE)
    public JdbcTemplate writeJdbcTemplate(@Qualifier(PersistenceQualifiers.Reporting.WRITE_DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    @Bean(name = PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE)
    public JdbcTemplate readJdbcTemplate(
            @Qualifier(PersistenceQualifiers.Reporting.READ_DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }



}
