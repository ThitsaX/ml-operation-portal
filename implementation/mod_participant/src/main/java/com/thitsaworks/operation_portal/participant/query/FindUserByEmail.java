package com.thitsaworks.operation_portal.participant.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import lombok.Value;

import java.util.Optional;

public interface FindUserByEmail {

    @Value
    class Input {

        private Email email;

    }

    @Value
    class Output {

        private Optional<ParticipantUserId> userId;

    }

    Output execute(Input input);

}
