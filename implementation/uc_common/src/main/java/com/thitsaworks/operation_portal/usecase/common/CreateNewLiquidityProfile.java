package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

import java.io.Serializable;
import java.util.List;

public abstract class CreateNewLiquidityProfile extends
                                                AbstractAuditableUseCase<CreateNewLiquidityProfile.Input, CreateNewLiquidityProfile.Output> {

    public record Input(
            ParticipantId participantId,
            List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record LiquidityProfileInfo(String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable {


        }

    }

    public record Output(
            boolean created) {


    }

}
