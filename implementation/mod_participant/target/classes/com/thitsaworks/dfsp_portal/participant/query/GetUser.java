package com.thitsaworks.dfsp_portal.participant.query;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.Value;

import java.time.Instant;

public interface GetUser {

    @Value
    class Input {

        private ParticipantUserId participantUserId;

    }

    @Value
    class Output {

        private ParticipantUserId participantUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private ParticipantId participantId;

        private Instant createdDate;

        private String dfspCode;

        private String dfspName;

    }

    Output execute(Input input) throws ParticipantUserNotFoundException;

}
