package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface CreateUser extends
                            UseCase<CreateUser.Input, CreateUser.Output> {

    record Input(String name,
                 Email email,
                 String password,
                 String firstName,
                 String lastName,
                 String jobTitle,
                 List<RoleId> roleIdList,
                 ParticipantId participantId,
                 PrincipalStatus status) { }

    record Output(UserId userId,
                  boolean created) { }

}
