package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllParticipant
        extends AbstractAuditableUseCase<GetAllParticipant.Input, GetAllParticipant.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

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

            private String dfsp_name;

            private String address;

            private Mobile mobile;

            private String businessContact;

            private String technicalContact;

            private Instant createdDate;

        }

    }

}
