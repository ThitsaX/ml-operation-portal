package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.approval.ApprovalConfiguration;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.settlement.SettlementConfiguration;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase")
@Import(
    value = {
        ParticipantConfiguration.class, AuditConfiguration.class, IAMConfiguration.class,
        HubServicesConfiguration.class, ApprovalConfiguration.class,
        ReportConfiguration.class, SchedulerConfiguration.class, SettlementConfiguration.class
    })
@RequiredArgsConstructor
public class OperationPortalUseCaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(OperationPortalUseCaseConfiguration.class);

    private final SchedulerEngine schedulerEngine;

    @PostConstruct
    public void bootstrapSchedulerEngine() {

        try {

            LOG.info("Starting SchedulerEngine bootstrap...");
            this.schedulerEngine.bootstrap();
            LOG.info("SchedulerEngine bootstrap completed successfully");

        } catch (Exception e) {

            LOG.error("Failed to bootstrap SchedulerEngine", e);
            throw new IllegalStateException("Failed to bootstrap SchedulerEngine", e);
        }
    }



}
