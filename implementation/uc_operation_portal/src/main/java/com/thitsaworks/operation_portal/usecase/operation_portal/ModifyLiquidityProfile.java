package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyLiquidityProfile extends
                                        UseCase<ModifyLiquidityProfile.Input, ModifyLiquidityProfile.Output> {

    record Input(ParticipantId participantId,
                 LiquidityProfileId liquidityProfileId,
                 String bankName,
                 String accountName,
                 String accountNumber,
                 String currency) { }

    record Output(boolean modified) { }

}
