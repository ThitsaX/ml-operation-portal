package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyExistingUser
        extends UseCase<ModifyExistingUser.Input, ModifyExistingUser.Output> {

    public record Input(
            ParticipantUserId participantUserId,
            String name,
            String firstName,
            String lastName,
            String jobTitle,
            UserRoleType userRoleType,
            PrincipalStatus principalStatus
    ) {}

    public record Output(
            boolean modified,
            ParticipantUserId participantUserId
    ) {}
}
