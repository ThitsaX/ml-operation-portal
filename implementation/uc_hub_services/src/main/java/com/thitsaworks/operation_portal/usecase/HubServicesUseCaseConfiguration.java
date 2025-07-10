package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.hub_services")
@Import(value = {ReportConfiguration.class, HubServicesConfiguration.class, AuditConfiguration.class})
public class HubServicesUseCaseConfiguration {

}
