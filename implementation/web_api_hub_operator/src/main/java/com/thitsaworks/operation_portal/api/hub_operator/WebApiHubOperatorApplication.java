package com.thitsaworks.operation_portal.api.hub_operator;

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

@Import(value = {HubOperatorApiConfiguration.class})
@SpringBootApplication(exclude = {
		HibernateJpaAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class WebApiHubOperatorApplication {

	public static void main(String[] args) {

		VaultConfiguration.Settings vaultSettings = VaultConfiguration.Settings.withPropertyOrEnv();

		SpringApplication.run(WebApiHubOperatorApplication.class, args);
	}

	@Bean
	public WebConfiguration.PortalPortSetting portSetting() {

		var portNo = System.getenv("HUB_OPERATOR_PORT_NO") == null ? "8003" : System.getenv("HUB_OPERATOR_PORT_NO");

		return new WebConfiguration.PortalPortSetting(Integer.parseInt(portNo));

	}

	@Bean
	public VaultConfiguration.Settings vaultSettings() {

		return VaultConfiguration.Settings.withPropertyOrEnv();
	}

}
