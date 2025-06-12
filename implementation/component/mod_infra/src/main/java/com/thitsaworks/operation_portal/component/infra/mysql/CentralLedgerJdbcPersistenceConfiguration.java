package com.thitsaworks.operation_portal.component.infra.mysql;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Import(value = {SharedDataSourceConfiguration.class})
public class CentralLedgerJdbcPersistenceConfiguration {

    public static final String READ_DB_POOL_SIZE = "overlap/mysql/read_db/pool_sizes";

    public static final String READ_DB_SETTINGS = "overlap/mysql/read_db/settings";

    public static final String WRITE_DB_POOL_SIZE = "overlap/mysql/write_db/pool_sizes";

    public static final String WRITE_DB_SETTINGS = "overlap/mysql/write_db/settings";

    @Bean(name = PersistenceQualifiers.central_ledger.JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier(PersistenceQualifiers.central_ledger.DATA_SOURCE) DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

//    @Bean(name = PersistenceQualifiers.central_ledger.WRITE_JDBC_TEMPLATE)
//    public JdbcTemplate writeJdbcTemplate(@Qualifier(PersistenceQualifiers.central_ledger.WRITE_DATA_SOURCE) DataSource dataSource) {
//
//        return new JdbcTemplate(dataSource);
//    }

    public record Settings(String url, String username, String password) { }

    public record PoolSizes(int minPool, int maxPool) { }

}
