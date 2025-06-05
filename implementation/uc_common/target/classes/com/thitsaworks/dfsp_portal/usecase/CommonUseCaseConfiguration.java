package com.thitsaworks.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.ledger.CentralLedgerConfiguration;
import com.thitsaworks.dfsp_portal.audit.AuditConfiguration;
import com.thitsaworks.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.dfsp_portal.usecase.common")
@Import(value = {
        ParticipantConfiguration.class, AuditConfiguration.class, IAMConfiguration.class,
        CentralLedgerConfiguration.class
})
public class CommonUseCaseConfiguration {

}
