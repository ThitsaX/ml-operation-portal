package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.component.common.identifier.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyParticipantCompanyShortName {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantId participantId;

        private String companyShortName;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean modified;

        private ParticipantId participantId;

    }

    Output execute(Input input) throws ParticipantNotFoundException;

}
