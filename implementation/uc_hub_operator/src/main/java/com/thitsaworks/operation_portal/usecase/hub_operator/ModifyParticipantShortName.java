package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;

public abstract class ModifyParticipantShortName extends
        AbstractAuditableUseCase<ModifyParticipantShortName.Input, ModifyParticipantShortName.Output> {

    public record Input(
            ParticipantId participantId,
            String companyShortName,
            AccessKey accessKey
    ) {}

    public record Output(
            boolean modified,
            ParticipantId participantId
    ) {}

}
