package com.thitsaworks.dfsp_portal.usecase.common;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

public abstract class ModifyExistingLiquidityProfile extends
        AbstractAuditableUseCase<ModifyExistingLiquidityProfile.Input, ModifyExistingLiquidityProfile.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantId participantId;

        List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LiquidityProfileInfo implements Serializable {

            private LiquidityProfileId liquidityProfileId;

            private String accountName;

            private String accountNumber;

            private String currency;

            private Boolean isActive;

        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private boolean modified;

    }

}
