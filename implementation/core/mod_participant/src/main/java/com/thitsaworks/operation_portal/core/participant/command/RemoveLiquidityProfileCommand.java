package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface RemoveLiquidityProfileCommand {

    Output execute(Input input) throws ParticipantException;

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId) { }

    record Output(boolean removed,
                  LiquidityProfileId liquidityProfileId) { }

}
