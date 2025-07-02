package com.thitsaworks.operation_portal.reporting.central_ledger;

import com.thitsaworks.operation_portal.component.infra.mysql.reporting.ReportingJdbcPersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {ReportingJdbcPersistenceConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.reporting.central_ledger")
public class CentralLedgerConfiguration {

}
