package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GetExistingParticipantUser extends
                                                 AbstractAuditableUseCase<GetExistingParticipantUser.Input, GetExistingParticipantUser.Output> {

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
    }

}
