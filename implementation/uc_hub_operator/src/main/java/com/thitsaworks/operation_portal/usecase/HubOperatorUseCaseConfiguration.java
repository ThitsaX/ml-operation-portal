package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.approval.ApprovalConfiguration;
import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.hub_operator")
@Import(
    value = {
        ParticipantConfiguration.class, HubUserConfiguration.class, AuditConfiguration.class,
        IAMConfiguration.class, ApprovalConfiguration.class})
public class HubOperatorUseCaseConfiguration {

}
