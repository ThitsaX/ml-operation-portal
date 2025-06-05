package com.thitsaworks.dfsp_portal.participant.query;

import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public interface GetOtherParticipants {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private List<ParticipantInfo> participantInfoList;

        @Value
        public static class ParticipantInfo implements Serializable {

            private ParticipantId participantId;

            private String dfsp_code;

            private String name;

        }

    }

    GetOtherParticipants.Output execute(GetOtherParticipants.Input input);

}
