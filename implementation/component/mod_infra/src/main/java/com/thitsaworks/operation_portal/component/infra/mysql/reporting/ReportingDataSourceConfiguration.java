package com.thitsaworks.operation_portal.component.infra.mysql.reporting;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class ReportingDataSourceConfiguration {

    public static final String FLYWAY_MIGRATION = "mysql/reporting//flyway/settings";

    public static final String WRITE_DB_SETTINGS_PATH = "mysql/reporting//write_db/settings";

    public static final String WRITE_DB_POOL_SIZE_PATH = "mysql/reporting//write_db/pool_sizes";

    public static final String READ_DB_SETTINGS_PATH = "mysql/reporting//read_db/settings";

    public static final String READ_DB_POOL_SIZE_PATH = "mysql/reporting//read_db/pool_sizes";

    @Bean(name = PersistenceQualifiers.Reporting.READ_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Reporting.READ_DATA_SOURCE)
    public DataSource readDataSource(@Qualifier(PersistenceQualifiers.Reporting.READ_SETTINGS) Settings settings) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Reporting-Read-Pool");
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

        config.setMaximumPoolSize(settings.maxPoolSize());
        config.setAutoCommit(false);

        return new HikariDataSource(config);

    }

    @Bean(name = PersistenceQualifiers.Reporting.WRITE_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Reporting.WRITE_DATA_SOURCE)
    public DataSource writeDataSource(
            @Qualifier(PersistenceQualifiers.Reporting.WRITE_SETTINGS) Settings settings) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Reporting-Write-Pool");
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

        config.setMaximumPoolSize(settings.maxPoolSize());
        config.setAutoCommit(false);

        return new HikariDataSource(config);

    }

    public record Settings(String url, String username, String password, int minPoolSize, int maxPoolSize) {}

}
