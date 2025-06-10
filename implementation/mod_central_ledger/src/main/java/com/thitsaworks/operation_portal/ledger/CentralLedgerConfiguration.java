package com.thitsaworks.operation_portal.ledger;

import com.thitsaworks.operation_portal.component.ComponentConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.ledger")
@Import(value = {
        ComponentConfiguration.class, CentralLedgerReadDbConfiguration.class})
public class CentralLedgerConfiguration {

}
