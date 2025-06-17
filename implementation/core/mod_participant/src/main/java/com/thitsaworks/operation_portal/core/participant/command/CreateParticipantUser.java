package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.EmailAlreadyRegisteredException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
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
