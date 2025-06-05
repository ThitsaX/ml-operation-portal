package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
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
