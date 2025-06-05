package com.thitsaworks.dfsp_portal.usecase;

import com.thitsaworks.dfsp_portal.audit.AuditConfiguration;
import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import com.thitsaworks.dfsp_portal.iam.IAMConfiguration;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.dfsp_portal.usecase.hub_operator")
@Import(value = {
        ParticipantConfiguration.class, HubUserConfiguration.class, AuditConfiguration.class, IAMConfiguration.class})
public class HubOperatorUseCaseConfiguration {

}
