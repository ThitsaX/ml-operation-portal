package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface ModifyUser
    extends UseCase<ModifyUser.Input, ModifyUser.Output> {

    record Input(UserId userId,
                 String name,
                 String firstName,
                 String lastName,
                 ParticipantId participantId,
                 String jobTitle,
                 List<RoleId> roleIdList) { }

    record Output(boolean modified,
                  UserId userId
    ) { }

}
