package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveLiquidityProfile extends UseCase<RemoveLiquidityProfile.Input, RemoveLiquidityProfile.Output> {

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId) { }

    record Output(boolean removed,
                  LiquidityProfileId liquidityProfileId) { }

}
