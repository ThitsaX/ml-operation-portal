package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.operation_portal.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.hub_operator")
@Import(value = {
        ParticipantConfiguration.class, HubUserConfiguration.class, AuditConfiguration.class, IAMConfiguration.class})
public class HubOperatorUseCaseConfiguration {

}
