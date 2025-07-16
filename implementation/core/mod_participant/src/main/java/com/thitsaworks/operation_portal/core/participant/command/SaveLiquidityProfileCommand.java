package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface SaveLiquidityProfileCommand {

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId,
                 String bankName,
                 String accountName,
                 String accountNumber,
                 String currency) { }

    record Output(boolean saved,
                  LiquidityProfileId liquidityProfileId) { }

    Output execute(Input input) throws ParticipantException;

}
