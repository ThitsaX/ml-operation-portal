package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface CreateLiquidityProfileCommand {

    record Input(ParticipantId participantId,
                 String bankName,
                 String accountName,
                 String accountNumber,
                 String currency,
                 boolean isActive) { }

    record Output(boolean created,
                  LiquidityProfileId liquidityProfileId) { }

    Output execute(Input input) throws ParticipantException;

}
