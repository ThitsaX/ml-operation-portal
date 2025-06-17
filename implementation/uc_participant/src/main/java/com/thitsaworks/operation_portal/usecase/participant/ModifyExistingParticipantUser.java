package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.component.common.type.UserRoleType;
import com.thitsaworks.component.common.type.PrincipalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ModifyExistingParticipantUser extends
                                                    AbstractAuditableUseCase<ModifyExistingParticipantUser.Input, ModifyExistingParticipantUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantUserId participantUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private UserRoleType userRoleType;

        private PrincipalStatus principalStatus;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private boolean modified;

        private ParticipantUserId participantUserId;

    }

}
