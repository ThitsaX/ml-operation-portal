package com.thitsaworks.operation_portal.participant.query;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetParticipants {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<ParticipantInfo> participantInfoList;

        @Value
        public static class ParticipantInfo implements Serializable {

            private ParticipantId participantId;

            private String dfsp_code;

            private String dfsp_name;

            private String name;

            private String address;

            private Mobile mobile;

            private String businessContact;

            private String technicalContact;

            private Instant createdDate;

        }

    }

    GetParticipants.Output execute(GetParticipants.Input input);

}
