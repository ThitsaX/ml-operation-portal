package com.thitsaworks.operation_portal.reporting.report;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CoreJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.hub.HubJpaPersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(
    value = {
        HubJdbcPersistenceConfiguration.class, HubJpaPersistenceConfiguration.class,
        CoreJdbcPersistenceConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.reporting.report")
public class ReportConfiguration {

}
