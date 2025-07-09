package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.audit.AuditConfiguration;

import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.common")
@Import(value = {
        ParticipantConfiguration.class, AuditConfiguration.class, IAMConfiguration.class,
        HubServicesConfiguration.class
})
public class CommonUseCaseConfiguration {

}
