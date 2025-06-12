package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.audit.AuditConfiguration;
import com.thitsaworks.operation_portal.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.participant")
@Import(value = {
        ParticipantConfiguration.class, AuditConfiguration.class, IAMConfiguration.class,
        HubOperatorUseCaseConfiguration.class})
public class ParticipantUseCaseConfiguration {

}
