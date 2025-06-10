package com.thitsaworks.operation_portal.datasource.persistence;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.thitsaworks.operation_portal.vault.VaultConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "readQueryFactory", transactionManagerRef = "readTransactionManager")
@Import(value = {
        VaultConfiguration.class})
public class MySqlReadDbConfiguration {

    @Primary
    @Bean(name = "readDataSource", destroyMethod = "close")
    public DataSource readDataSource(Settings settings) {

        HikariConfig config = new HikariConfig();

        config.setPoolName("Hikari-Read");
        config.setJdbcUrl(settings.url);
        config.setUsername(settings.username);
        config.setPassword(settings.password);
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

        config.setMaximumPoolSize(settings.maxPoolSize);

        return new HikariDataSource(config);

    }

    @Bean(name = "readTransactionManager")
    public DataSourceTransactionManager readTransactionManager(@Qualifier("readDataSource") DataSource readDataSource) {

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();

        transactionManager.setDataSource(readDataSource);

        return transactionManager;

    }

    @Primary
    @Bean(name = "readQuerydslConfiguration")
    public com.querydsl.sql.Configuration readQuerydslConfiguration() {

        SQLTemplates templates = MySQLTemplates.builder().build();

        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);

        //configuration.setExceptionTranslator(new SpringExceptionTranslator());
        //configuration.register(new DateTimeType());
        //configuration.register(new LocalDateType());

        return configuration;

    }

    @Primary
    @Bean(name = "readQueryFactory")
    public SQLQueryFactory readQueryFactory(com.querydsl.sql.Configuration commonDbQuerydslConfiguration,
                                            @Qualifier("readDataSource") DataSource readDataSource) {

        SpringConnectionProvider provider = new SpringConnectionProvider(readDataSource);

        return new SQLQueryFactory(commonDbQuerydslConfiguration, provider);

    }

    @Bean(name = "dfspJdbcTemplate")
    public JdbcTemplate dfspJdbcTemplate(
            @Qualifier("readDataSource") DataSource readDataSource) {

        return new JdbcTemplate(readDataSource);
    }


    @NoArgsConstructor
    @Getter
    @Setter
    public static class Settings {

        String url;

        String username;

        String password;

        String schema;

        String showSql;

        String formatSql;

        int minPoolSize;

        int maxPoolSize;

    }

}
