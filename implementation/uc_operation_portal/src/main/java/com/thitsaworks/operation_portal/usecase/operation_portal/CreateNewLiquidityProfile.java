package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateNewLiquidityProfile extends
                                           UseCase<CreateNewLiquidityProfile.Input, CreateNewLiquidityProfile.Output> {

    record Input(ParticipantId participantId,
                 String bankName,
                 String accountName,
                 String accountNumber,
                 String currency) { }

    record Output(boolean created,
                  LiquidityProfileId liquidityProfileId) { }

}
