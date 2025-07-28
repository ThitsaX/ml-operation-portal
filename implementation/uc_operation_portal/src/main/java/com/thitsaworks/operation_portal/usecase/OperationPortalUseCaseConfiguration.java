package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.approval.ApprovalConfiguration;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;

import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase")
@Import(
    value = {
        ParticipantConfiguration.class, AuditConfiguration.class, IAMConfiguration.class,
        HubServicesConfiguration.class, HubUserConfiguration.class, ApprovalConfiguration.class,
        ReportConfiguration.class, SchedulerConfiguration.class
    })
public class OperationPortalUseCaseConfiguration {

}
