package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyUser
        extends UseCase<ModifyUser.Input, ModifyUser.Output> {

    public record Input(
            ParticipantUserId participantUserId,
            String name,
            String firstName,
            String lastName,
            String jobTitle,
            PrincipalStatus principalStatus
    ) {}

    public record Output(
            boolean modified,
            ParticipantUserId participantUserId
    ) {}
}
