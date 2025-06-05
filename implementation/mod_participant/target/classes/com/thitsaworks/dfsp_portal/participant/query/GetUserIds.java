package com.thitsaworks.dfsp_portal.participant.query;

import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.Value;

import java.util.List;

public interface GetUserIds {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private List<ParticipantUserId> participantUserIds;

    }

    Output execute(Input input);

}
