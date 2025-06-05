package com.thitsa.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.ledger.CentralLedgerConfiguration;
import com.thitsa.dfsp_portal.report.ReportConfiguration;
import com.thitsaworks.dfsp_portal.audit.AuditConfiguration;
import com.thitsaworks.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsa.dfsp_portal.usecase.central_ledger")
@Import(value = {AuditConfiguration.class, IAMConfiguration.class, ParticipantConfiguration.class,CentralLedgerConfiguration.class,ReportConfiguration.class})
public class CentralLedgerUseCaseConfiguration {

}
