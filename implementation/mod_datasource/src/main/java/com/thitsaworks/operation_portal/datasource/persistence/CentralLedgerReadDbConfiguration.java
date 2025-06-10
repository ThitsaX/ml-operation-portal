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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "centralLedgerReadQueryFactory",
        transactionManagerRef = "centralLedgerReadTransactionManager",
        basePackages = {
                "com.thitsa.*"
        })
@Import(value = {
        VaultConfiguration.class
})
public class CentralLedgerReadDbConfiguration {

    @Bean(name = "centralLedgerReadDataSource", destroyMethod = "close")
    @Qualifier("centralLedgerReadDataSource")
    public DataSource centralLedgerReadDataSource(CentralLedgerReadDbConfiguration.Settings settings) {

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

    @Bean(name = "centralLedgerReadTransactionManager")
    public DataSourceTransactionManager centralLedgerReadTransactionManager(
            @Qualifier("centralLedgerReadDataSource") DataSource centralLedgerReadDataSource) {

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();

        transactionManager.setDataSource(centralLedgerReadDataSource);

        return transactionManager;

    }

    @Bean(name = "centralLedgerReadQuerydslConfiguration")
    public com.querydsl.sql.Configuration centralLedgerReadQuerydslConfiguration() {

        SQLTemplates templates = MySQLTemplates.builder().build();

        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);

        //configuration.setExceptionTranslator(new SpringExceptionTranslator());
        //configuration.register(new DateTimeType());
        //configuration.register(new LocalDateType());

        return configuration;

    }

    @Bean(name = "centralLedgerReadQueryFactory")
    public SQLQueryFactory centralLedgerReadQueryFactory(com.querydsl.sql.Configuration commonDbQuerydslConfiguration,
                                                         @Qualifier("centralLedgerReadDataSource")
                                                         DataSource centralLedgerReadDataSource) {

        SpringConnectionProvider provider = new SpringConnectionProvider(centralLedgerReadDataSource);

        return new SQLQueryFactory(commonDbQuerydslConfiguration, provider);

    }

    @Bean(name = "centralLedgerJdbcTemplate")
    @Qualifier("centralLedgerJdbcTemplate")
    public JdbcTemplate centralLedgerJdbcTemplate(
            @Qualifier("centralLedgerReadDataSource") DataSource centralLedgerReadDataSource) {

        return new JdbcTemplate(centralLedgerReadDataSource);
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
