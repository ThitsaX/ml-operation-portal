package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

public interface CreateLiquidityProfile {

    record Input(
            ParticipantId participantId,
            String accountName,
            String accountNumber,
            String currency,
            Boolean isActive) {}

    record Output(boolean created) {}

    Output execute(Input input) throws ParticipantNotFoundException;

}
