package com.thitsa.dfsp_portal.ledger;

import com.thitsaworks.dfsp_portal.component.ComponentConfiguration;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsa.dfsp_portal.ledger")
@Import(value = {
        ComponentConfiguration.class, CentralLedgerReadDbConfiguration.class})
public class CentralLedgerConfiguration {

}
