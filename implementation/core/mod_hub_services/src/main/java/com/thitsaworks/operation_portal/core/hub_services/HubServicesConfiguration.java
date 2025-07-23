package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {ReportingJdbcPersistenceConfiguration.class, MiscConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.core.hub_services")
public class HubServicesConfiguration {

    @Bean
    public HubServicesConfiguration.Settings hubServiceConfigurationSettings() {

        return new HubServicesConfiguration.Settings(System.getProperty("CENTRAL_LEDGER_ENDPOINT"));

    }

    public record Settings(String centralLedgerEndpoint) {}

}
