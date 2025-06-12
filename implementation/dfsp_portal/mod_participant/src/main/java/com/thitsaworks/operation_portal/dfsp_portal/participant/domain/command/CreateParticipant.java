package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.type.DfspCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateParticipant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String name;

        private DfspCode dfspCode;

        private String dfspName;

        private String address;

        private Mobile mobile;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

        private ParticipantId participantId;

    }

    Output execute(Input input) throws Exception;

}
