package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.component.common.identifier.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyLiquidityProfile {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private ParticipantId participantId;

        private LiquidityProfileId liquidityProfileId;

        private String accountName;

        private String accountNumber;

        private String currency;

        private Boolean isActive;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private LiquidityProfileId liquidityProfileId;

        private boolean modified;

    }

    Output execute(Input input) throws ParticipantNotFoundException,
            LiquidityProfileNotFoundException;

}
