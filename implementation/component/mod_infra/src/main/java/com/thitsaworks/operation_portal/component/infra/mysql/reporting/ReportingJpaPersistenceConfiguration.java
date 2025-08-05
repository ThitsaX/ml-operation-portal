package com.thitsaworks.operation_portal.component.infra.mysql.reporting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.FlushMode;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Import(value = {ReportingDataSourceConfiguration.class})
@EnableJpaRepositories(
        basePackages = "com.thitsaworks.operation_portal.Reporting.report.*",
        enableDefaultTransactions = false,
        considerNestedRepositories = true,
        transactionManagerRef = PersistenceQualifiers.Reporting.TRANSACTION_MANAGER,
        entityManagerFactoryRef = PersistenceQualifiers.Reporting.ENTITY_MANAGER_FACTORY)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ReportingJpaPersistenceConfiguration {

    @Bean(name = PersistenceQualifiers.Reporting.ENTITY_MANAGER_FACTORY)
    @Qualifier(PersistenceQualifiers.Reporting.ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(PersistenceQualifiers.Reporting.WRITE_DATA_SOURCE) DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        entityManagerFactoryBean.setPackagesToScan("com.thitsaworks.operation_portal.reporting.**");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.dialect", MySQLDialect.class);
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.format_sql", false);
        jpaProperties.put("hibernate.hibernate.connection.provider_disables_autocommit", true);
        jpaProperties.put("hibernate.flushMode", FlushMode.MANUAL.name());

        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        entityManagerFactoryBean.setPersistenceUnitName("reporting");

        return entityManagerFactoryBean;

    }

    @Bean(name = PersistenceQualifiers.Reporting.TRANSACTION_MANAGER)
    @Qualifier(PersistenceQualifiers.Reporting.TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(PersistenceQualifiers.Reporting.ENTITY_MANAGER_FACTORY)
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);

    }

    @Bean(name = PersistenceQualifiers.Reporting.JPA_QUERY_FACTORY)
    @Qualifier(PersistenceQualifiers.Reporting.JPA_QUERY_FACTORY)
    public JPAQueryFactory readQueryFactory(
            @Qualifier(PersistenceQualifiers.Reporting.ENTITY_MANAGER_FACTORY) EntityManager entityManager) {

        return new JPAQueryFactory(entityManager);

    }

    public record Settings(String url, String username, String password) { }

    public record PoolSizes(int minPool, int maxPool) { }

}
