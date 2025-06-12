package com.thitsaworks.operation_portal.component.infra.mysql;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class SharedDataSourceConfiguration {

    public static final String FLYWAY_MIGRATION = "mysql/shared//flyway/settings";

    public static final String WRITE_DB_SETTINGS_PATH = "mysql/shared//write_db/settings";

    public static final String WRITE_DB_POOL_SIZE_PATH = "mysql/shared//write_db/pool_sizes";

    public static final String READ_DB_SETTINGS_PATH = "mysql/shared//read_db/settings";

    public static final String READ_DB_POOL_SIZE_PATH = "mysql/shared//read_db/pool_sizes";

    @Bean(name = PersistenceQualifiers.Shared.READ_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Shared.READ_DATA_SOURCE)
    public DataSource readDataSource(@Qualifier(PersistenceQualifiers.Shared.READ_SETTINGS) Settings settings,
                                     @Qualifier(PersistenceQualifiers.Shared.READ_POOL_SIZES) PoolSizes poolSizes) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Shared-Read-Pool");
        config.setJdbcUrl(settings.url());
        config.setUsername(settings.username());
        config.setPassword(settings.password());
        config.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);

        config.setMaximumPoolSize(poolSizes.maxPool());
        config.setAutoCommit(false);

        return new HikariDataSource(config);

    }

    @Bean(name = PersistenceQualifiers.Shared.WRITE_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Shared.WRITE_DATA_SOURCE)
    public DataSource writeDataSource(
        @Qualifier(PersistenceQualifiers.Shared.WRITE_SETTINGS) Settings settings,
        @Qualifier(PersistenceQualifiers.Shared.WRITE_POOL_SIZES) PoolSizes poolSizes) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Shared-Write-Pool");
        config.setJdbcUrl(settings.url());
        config.setUsername(settings.username());
        config.setPassword(settings.password());
        config.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);

        config.setMaximumPoolSize(poolSizes.maxPool());
        config.setAutoCommit(false);

        return new HikariDataSource(config);

    }

    public record Settings(String url, String username, String password) { }

    public record PoolSizes(int minPool, int maxPool) { }

}
