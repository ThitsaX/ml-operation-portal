package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;

public interface ModifyExistingParticipantUser extends UseCase<ModifyExistingParticipantUser.Input, ModifyExistingParticipantUser.Output> {

    record Input(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            UserRoleType userRoleType,
            PrincipalStatus principalStatus
    ) {}

    record Output(
            boolean modified,
            ParticipantUserId participantUserId
    ) {}

}
