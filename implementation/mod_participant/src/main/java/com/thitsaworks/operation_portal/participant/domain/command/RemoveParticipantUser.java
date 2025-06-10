package com.thitsaworks.operation_portal.participant.domain.command;

import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface RemoveParticipantUser {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantId participantId;

        private ParticipantUserId participantUserId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private ParticipantUserId participantUserId;

        private boolean removed;

    }

    RemoveParticipantUser.Output execute(RemoveParticipantUser.Input input) throws ParticipantNotFoundException,
            ParticipantUserNotFoundException;

}
