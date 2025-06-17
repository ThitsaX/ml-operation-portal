package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.component.common.identifier.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateLiquidityProfile {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        protected ParticipantId participantId;

        protected String accountName;

        protected String accountNumber;


        protected String currency;

        protected Boolean isActive;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

    }

    Output execute(Input input) throws ParticipantNotFoundException;

}
