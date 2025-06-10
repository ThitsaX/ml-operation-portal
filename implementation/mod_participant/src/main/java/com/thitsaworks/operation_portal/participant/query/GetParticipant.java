package com.thitsaworks.operation_portal.participant.query;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.Value;

import java.time.Instant;

public interface GetParticipant {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private ParticipantId participantId;

        private String dfsp_code;

        private String name;

        private String address;

        private Mobile mobile;
        private ContactId businessContactId;

        private ContactId technicalContactId;

        private Instant createdDate;

    }

    GetParticipant.Output execute(GetParticipant.Input input) throws ParticipantNotFoundException;

}
