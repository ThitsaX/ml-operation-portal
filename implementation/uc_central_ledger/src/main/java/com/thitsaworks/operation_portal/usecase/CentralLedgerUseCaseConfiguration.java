package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.reporting.central_ledger.CentralLedgerConfiguration;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.central_ledger")
@Import(value = {CentralLedgerConfiguration.class, ReportConfiguration.class, AuditConfiguration.class})
public class CentralLedgerUseCaseConfiguration {

}
