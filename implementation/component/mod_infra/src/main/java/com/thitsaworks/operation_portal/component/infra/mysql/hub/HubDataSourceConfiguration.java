package com.thitsaworks.operation_portal.component.infra.mysql.hub;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class HubDataSourceConfiguration {

    public static final String WRITE_DB_SETTINGS_PATH = "mysql/hub_data/write_db/settings";

    public static final String READ_DB_SETTINGS_PATH = "mysql/hub_data/read_db/settings";

    @Bean(name = PersistenceQualifiers.Hub.READ_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Hub.READ_DATA_SOURCE)
    public DataSource readDataSource(@Qualifier(PersistenceQualifiers.Hub.READ_SETTINGS) Settings settings) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Hub-Read-Pool");
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

    @Bean(name = PersistenceQualifiers.Hub.WRITE_DATA_SOURCE)
    @Qualifier(PersistenceQualifiers.Hub.WRITE_DATA_SOURCE)
    public DataSource writeDataSource(
        @Qualifier(PersistenceQualifiers.Hub.WRITE_SETTINGS) Settings settings) {

        var config = new HikariConfig();

        config.setPoolName("Hikari-Hub-Write-Pool");
        config.setJdbcUrl(settings.url());
        config.setUsername(settings.username());
        config.setPassword(settings.password());
        config.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        config.setKeepaliveTime(30000);

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
        config.setAutoCommit(true);

        return new HikariDataSource(config);

    }

    public record Settings(String url, String username, String password, int minPoolSize, int maxPoolSize) { }

}
