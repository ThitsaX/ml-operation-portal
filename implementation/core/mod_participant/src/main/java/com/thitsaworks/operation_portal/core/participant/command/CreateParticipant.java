package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.type.DfspCode;
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
