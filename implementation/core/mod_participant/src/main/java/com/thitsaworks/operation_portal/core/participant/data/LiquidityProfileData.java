package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;

import java.io.Serializable;

public record LiquidityProfileData(
        LiquidityProfileId liquidityProfileId,

        ParticipantId participantId,

        String accountName,

        String accountNumber,

        String currency,

        Boolean isActive) implements Serializable {

    public LiquidityProfileData(LiquidityProfile liquidityProfile) {

        this(liquidityProfile.getLiquidityProfileId(),

             liquidityProfile.getParticipant().getParticipantId(),

             liquidityProfile.getAccountName(),

             liquidityProfile.getAccountNumber(),

             liquidityProfile.getCurrency(),

             liquidityProfile.getIsActive());

    }

}
