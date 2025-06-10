package com.thitsaworks.operation_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.exception.ContactNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyContact {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantId participantId;

        private ContactId contactId;

        private String name;

        private String title;

        private Email email;

        private Mobile mobile;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean modified;

        private ContactId contactId;

    }

    Output execute(Input input) throws ParticipantNotFoundException, ContactNotFoundException;

}
