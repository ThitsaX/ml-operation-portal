package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

public abstract class RemoveExistingParticipantUser extends
        AbstractAuditableUseCase<RemoveExistingParticipantUser.Input, RemoveExistingParticipantUser.Output> {

    public record Input(
            ParticipantId participantId,
            ParticipantUserId participantUserId
    ) {}

    public record Output(
            boolean removed,
            ParticipantUserId participantUserId
    ) {}

}
