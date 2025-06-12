package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.ledger.CentralLedgerConfiguration;
import com.thitsaworks.operation_portal.report.ReportConfiguration;
import com.thitsaworks.operation_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.central_ledger")
@Import(value = {AuditConfiguration.class, IAMConfiguration.class, ParticipantConfiguration.class,CentralLedgerConfiguration.class,ReportConfiguration.class})
public class CentralLedgerUseCaseConfiguration {

}
