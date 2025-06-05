package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyParticipant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantId participantId;

        private String companyName;

       // private String companyShortName;

        private String address;

        private Mobile mobile;

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
