package com.thitsaworks.dfsp_portal.usecase.common;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ContactId;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetExistingParticipant extends
        AbstractAuditableUseCase<GetExistingParticipant.Input, GetExistingParticipant.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantId participantId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private ParticipantId participantId;

        private String dfsp_code;

        private String name;

        private String address;

        private Mobile mobile;

        private Instant createdDate;

        private List<ContactInfo> contactInfoList;

        private List<LiquidityProfileInfo> liquidityProfileInfoList;

        @Value
        public static class ContactInfo implements Serializable {

            private ContactId contactId;

            private String name;

            private String title;

            private Email email;

            private Mobile mobile;

            private String contactType;

        }

        @Value
        public static class LiquidityProfileInfo implements Serializable {

            private LiquidityProfileId liquidityProfileId;

            private String accountName;

            private String accountNumber;

            private String currency;

            private Boolean isActive;

        }

    }

}
