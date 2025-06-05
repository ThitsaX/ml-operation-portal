package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GetUserProfile extends
        AbstractOwnableUseCase<GetUserProfile.Input, GetUserProfile.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantUserId participantUserId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private ParticipantUserId participantUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private ParticipantId participantId;

        private Long createdDate;

        private String dfspCode;

        private String dfspName;

        private String roleType;

    }

}
