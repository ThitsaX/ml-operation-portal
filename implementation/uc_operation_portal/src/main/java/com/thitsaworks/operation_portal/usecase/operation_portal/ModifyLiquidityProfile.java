package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.util.List;

public interface ModifyLiquidityProfile extends
                                                UseCase<ModifyLiquidityProfile.Input, ModifyLiquidityProfile.Output> {

    record Input(ParticipantId participantId,
                 List<Input.LiquidityProfileInfo> liquidityProfileInfoList) {

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId,
                                           String bankName,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable { }

    }

    record Output(boolean modified) { }

}
