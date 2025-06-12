package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateContact {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String name;

        private String title;

        private Email email;

        private Mobile mobile;

        private ParticipantId participantId;

        private String contactType;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

    }

    Output execute(Input input) throws ParticipantNotFoundException;
}
