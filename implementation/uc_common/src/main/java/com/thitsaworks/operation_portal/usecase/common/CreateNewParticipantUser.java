package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class CreateNewParticipantUser extends
                                               AbstractAuditableUseCase<CreateNewParticipantUser.Input, CreateNewParticipantUser.Output> {

    public record Input(
            String username,
            String name,
            Email email,
            String password,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId,
            UserRoleType userRoleType,
            RealmType realmType,
            PrincipalStatus activeStatus) {}

    public record Output(boolean created) {}

}
