package com.thitsaworks.operation_portal.central_ledger.report;

import com.thitsaworks.operation_portal.component.infra.mysql.CentralLedgerJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.CentralLedgerJpaPersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {CentralLedgerJdbcPersistenceConfiguration.class, CentralLedgerJpaPersistenceConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.central_ledger.report")
public class ReportConfiguration {

}
