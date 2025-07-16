package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface SaveLiquidityProfile extends UseCase<SaveLiquidityProfile.Input, SaveLiquidityProfile.Output> {

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId,
                 String bankName,
                 String accountName,
                 String accountNumber,
                 String currency) { }

    record Output(boolean saved,
                  LiquidityProfileId liquidityProfileId) { }

}
