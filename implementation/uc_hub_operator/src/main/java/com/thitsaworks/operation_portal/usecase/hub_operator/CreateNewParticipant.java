package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

public abstract class CreateNewParticipant
        extends AbstractAuditableUseCase<CreateNewParticipant.Input, CreateNewParticipant.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String name;

        private DfspCode dfspCode;

        private String dfspName;

        private String address;

        private Mobile mobile;

        private List<ContactInfo> contactInfoList;

        private List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Value
        public static class ContactInfo implements Serializable {

            private String name;

            private String title;

            private Email email;

            private Mobile mobile;

            private String contactType;

        }

        @Value
        public static class LiquidityProfileInfo implements Serializable {

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

        private boolean created;

        private ParticipantId participantId;

    }

}
