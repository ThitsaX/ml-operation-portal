package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyParticipantUser {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantUserId participantUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private ParticipantId participantId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private ParticipantUserId participantUserId;

        private boolean modified;

    }

    ModifyParticipantUser.Output execute(ModifyParticipantUser.Input input) throws ParticipantUserNotFoundException;
}
