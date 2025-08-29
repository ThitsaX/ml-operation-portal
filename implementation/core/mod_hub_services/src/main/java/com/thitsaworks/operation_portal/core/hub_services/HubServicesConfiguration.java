package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mongo.ReportingMongoConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {ReportingJdbcPersistenceConfiguration.class, MiscConfiguration.class, ReportingMongoConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.core.hub_services")
public class HubServicesConfiguration {

    public static final String HUB_SERVICE_SETTINGS_PATH = "hub_services/settings";

    public record Settings(String centralLedgerServiceEndpoint, String centralSettlementServiceEndpoint) {}

}
