package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.iam.type.RealmType;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class CreateNewParticipantUser extends
        AbstractAuditableUseCase<CreateNewParticipantUser.Input, CreateNewParticipantUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String name;

        private Email email;

        private String password;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private ParticipantId participantId;

        private UserRoleType userRoleType;

        private RealmType realmType;

        private PrincipalStatus activeStatus;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {


        private boolean created;

    }

}
