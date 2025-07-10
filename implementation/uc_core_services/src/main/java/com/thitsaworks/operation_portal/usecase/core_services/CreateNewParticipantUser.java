package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface CreateNewParticipantUser extends
                                          UseCase<CreateNewParticipantUser.Input, CreateNewParticipantUser.Output> {

    record Input(String name,
                 Email email,
                 String password,
                 String firstName,
                 String lastName,
                 String jobTitle,
                 ParticipantId participantId,
                 UserRoleType userRoleType,
                 RealmType realmType,
                 PrincipalStatus activeStatus) { }

    record Output(boolean created) { }

}
