package com.thitsaworks.operation_portal.component.infra.mysql.core;

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

@Import(value = {CoreDataSourceConfiguration.class})
@EnableJpaRepositories(
        basePackages = "com.thitsaworks.operation_portal.core.*",
    enableDefaultTransactions = false,
    considerNestedRepositories = true,
        transactionManagerRef = PersistenceQualifiers.Core.TRANSACTION_MANAGER,
        entityManagerFactoryRef = PersistenceQualifiers.Core.ENTITY_MANAGER_FACTORY)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CorePersistenceConfiguration {

    @Bean(name = PersistenceQualifiers.Core.ENTITY_MANAGER_FACTORY)
    @Qualifier(PersistenceQualifiers.Core.ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(PersistenceQualifiers.Core.WRITE_DATA_SOURCE) DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        entityManagerFactoryBean.setPackagesToScan("com.thitsaworks.operation_portal.core.**");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.dialect", MySQLDialect.class);
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.format_sql", false);
        jpaProperties.put("hibernate.hibernate.connection.provider_disables_autocommit", true);
        jpaProperties.put("hibernate.flushMode", FlushMode.MANUAL.name());

        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        entityManagerFactoryBean.setPersistenceUnitName("core");

        return entityManagerFactoryBean;

    }

    @Bean(name = PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    @Qualifier(PersistenceQualifiers.Core.TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(PersistenceQualifiers.Core.ENTITY_MANAGER_FACTORY)
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);

    }

    @Primary
    @Bean(name = PersistenceQualifiers.Core.JPA_QUERY_FACTORY)
    @Qualifier(PersistenceQualifiers.Core.JPA_QUERY_FACTORY)
    public JPAQueryFactory readQueryFactory(
            @Qualifier(PersistenceQualifiers.Core.ENTITY_MANAGER_FACTORY) EntityManager entityManager) {

        return new JPAQueryFactory(entityManager);

    }

    public record Settings(String url, String username, String password) { }

    public record PoolSizes(int minPool, int maxPool) { }

}
