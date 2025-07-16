package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.util.List;

public interface GetLiquidityProfileList
    extends UseCase<GetLiquidityProfileList.Input, GetLiquidityProfileList.Output> {

    record Input(ParticipantId participantId) { }

    record Output(List<LiquidityProfileInfo> liquidityProfileInfoList) {

        public record LiquidityProfileInfo(LiquidityProfileId liquidityProfileId,
                                           String bankName,
                                           String accountName,
                                           String accountNumber,
                                           String currency,
                                           Boolean isActive) implements Serializable { }

    }

}
