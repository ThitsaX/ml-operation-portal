package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mongo.ReportingMongoConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {ReportingJdbcPersistenceConfiguration.class, MiscConfiguration.class, ReportingMongoConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.core.hub_services")
public class HubServicesConfiguration {

    @Bean
    public HubServicesConfiguration.Settings hubServiceConfigurationSettings() {

        return new HubServicesConfiguration.Settings(System.getProperty("CENTRAL_LEDGER_ENDPOINT"),
                                                     System.getProperty("SETTLEMENT_ENDPOINT"
                                                                            ));

    }

    public record Settings(String centralLedgerEndpoint, String settlementEndpoint) {}

}
