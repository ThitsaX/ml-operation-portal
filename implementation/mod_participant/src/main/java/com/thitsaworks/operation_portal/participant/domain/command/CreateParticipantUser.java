package com.thitsaworks.operation_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.participant.exception.EmailAlreadyRegisteredException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateParticipantUser {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String name;

        private Email email;

        private ParticipantId participantId;

        private String firstName;

        private String lastName;

        private String jobTitle;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

        private ParticipantUserId participantUserId;

    }

    Output execute(Input input) throws ParticipantNotFoundException, EmailAlreadyRegisteredException;

}
