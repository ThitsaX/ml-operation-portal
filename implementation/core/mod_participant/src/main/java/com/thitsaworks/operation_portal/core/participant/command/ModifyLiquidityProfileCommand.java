package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyLiquidityProfileCommand {

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId,
                 String accountName,
                 String accountNumber,
                 String currency,
                 Boolean isActive) {}

    record Output(LiquidityProfileId liquidityProfileId,
                  boolean modified) {}

    Output execute(Input input) throws ParticipantException;

}
