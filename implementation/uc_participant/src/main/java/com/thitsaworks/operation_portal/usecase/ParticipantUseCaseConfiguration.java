package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.core.iam.IAMConfiguration;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.usecase.participant")
@Import(value = {
        ParticipantConfiguration.class, IAMConfiguration.class})
public class ParticipantUseCaseConfiguration {

}
