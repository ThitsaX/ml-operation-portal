package com.thitsaworks.operation_portal.central_ledger.ledger;

import com.thitsaworks.operation_portal.component.infra.mysql.CentralLedgerJdbcPersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.CentralLedgerJpaPersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(value = {CentralLedgerJdbcPersistenceConfiguration.class})
@ComponentScan("com.thitsaworks.operation_portal.central_ledger.ledger")
public class CentralLedgerConfiguration {

}
