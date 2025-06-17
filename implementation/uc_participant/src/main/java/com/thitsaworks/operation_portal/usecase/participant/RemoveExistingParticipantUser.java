package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class RemoveExistingParticipantUser extends
                                                    AbstractAuditableUseCase<RemoveExistingParticipantUser.Input, RemoveExistingParticipantUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantId participantId;

        private ParticipantUserId participantUserId;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private boolean removed;

        private ParticipantUserId participantUserId;

    }

}
