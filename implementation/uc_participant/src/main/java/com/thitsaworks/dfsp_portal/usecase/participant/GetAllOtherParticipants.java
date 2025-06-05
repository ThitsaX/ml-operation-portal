package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public abstract class GetAllOtherParticipants
        extends AbstractOwnableUseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output> {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Input {

        private ParticipantId participantId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private List<ParticipantInfo> participantInfoList;

        @Value
        public static class ParticipantInfo implements Serializable {

            private ParticipantId participantId;

            private String dfsp_code;

            private String name;


        }

    }

}
