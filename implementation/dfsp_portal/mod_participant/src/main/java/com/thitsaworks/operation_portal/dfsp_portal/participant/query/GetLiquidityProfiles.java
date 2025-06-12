package com.thitsaworks.operation_portal.dfsp_portal.participant.query;

import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public interface GetLiquidityProfiles {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private List<Output.LiquidityProfileInfo> liquidityProfileInfoList;

        @Value
        public static class LiquidityProfileInfo implements Serializable {

            private ParticipantId participantId;

            private LiquidityProfileId liquidityProfileId;

            private String accountName;

            private String accountNumber;

            private String currency;

            private Boolean isActive;

        }

    }

    Output execute(Input input) throws ParticipantNotFoundException;

}
