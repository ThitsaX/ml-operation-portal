package com.thitsaworks.operation_portal.reporting.report;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJpaPersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {ReportingJdbcPersistenceConfiguration.class, ReportingJpaPersistenceConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.Reporting.report")
public class ReportConfiguration {

}
