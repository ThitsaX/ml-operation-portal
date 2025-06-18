package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

import java.io.Serializable;
import java.util.List;

public abstract class ModifyExistingLiquidityProfile extends
                                                     AbstractAuditableUseCase<ModifyExistingLiquidityProfile.Input, ModifyExistingLiquidityProfile.Output> {

    public record Input(ParticipantId participantId, List<Input.LiquidityProfileInfo> liquidityProfileInfoList) {

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId, String accountName,
                                           String accountNumber,
                                           String currency, Boolean isActive) implements Serializable {}

    }

    public record Output(boolean modified) {}

}
