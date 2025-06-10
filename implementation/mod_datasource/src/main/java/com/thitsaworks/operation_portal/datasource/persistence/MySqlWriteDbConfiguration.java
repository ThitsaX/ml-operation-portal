package com.thitsaworks.operation_portal.datasource.persistence;

import com.thitsaworks.operation_portal.vault.VaultConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.dialect.MySQL8Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(
        basePackages = "com.thitsaworks.*",
        entityManagerFactoryRef = "writeEntityManagerFactory",
        transactionManagerRef = "writeTransactionManager",
        considerNestedRepositories = true)
@EnableTransactionManagement
@Import(value = {
        VaultConfiguration.class
})
public class MySqlWriteDbConfiguration {

    @Bean(name = "writeDataSource", destroyMethod = "close")
    public DataSource writeDataSource(Settings settings) {

        HikariConfig config = new HikariConfig();

        config.setPoolName("Hikari-Write");
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

    @Bean(name = "writeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(
            @Qualifier("writeDataSource") DataSource writeDataSource, Settings settings) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(writeDataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        entityManagerFactoryBean.setPackagesToScan("com.thitsaworks.operation_portal.**");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.dialect", MySQL8Dialect.class.getName());
        jpaProperties.put("hibernate.show_sql", settings.showSql);
        jpaProperties.put("hibernate.format_sql", settings.formatSql);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;

    }

    @Bean(name = "writeTransactionManager")
    @Primary
    public JpaTransactionManager writeTransactionManager(
            @Qualifier("writeEntityManagerFactory") EntityManagerFactory writeEntityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(writeEntityManagerFactory);

        return transactionManager;

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