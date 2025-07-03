package com.thitsaworks.operation_portal.api.participant;

import com.thitsaworks.operation_portal.component.infra.flyway.DatabaseMigration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingDataSourceConfiguration;
import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(value = {ParticipantApiConfiguration.class})
@SpringBootApplication(exclude = {
        HibernateJpaAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class WebApiParticipantApplication {

    public static void main(String[] args) {

        VaultConfiguration.Settings vaultSettings = VaultConfiguration.Settings.withPropertyOrEnv();

        DatabaseMigration.migrate(CoreDataSourceConfiguration.FLYWAY_MIGRATION, vaultSettings);
        DatabaseMigration.migrate(ReportingDataSourceConfiguration.FLYWAY_MIGRATION, vaultSettings);

        SpringApplication.run(WebApiParticipantApplication.class, args);
    }

    @Bean
    public WebConfiguration.PortalPortSetting portSetting() {

        var portNo = System.getenv("PARTICIPANT_PORT_NO") == null ? "8002" : System.getenv("PARTICIPANT_PORT_NO");

        return new WebConfiguration.PortalPortSetting(Integer.parseInt(portNo));
    }

    @Bean
    public VaultConfiguration.Settings vaultSettings() {

        return VaultConfiguration.Settings.withPropertyOrEnv();
    }

}
