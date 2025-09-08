package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GetUser extends UseCase<GetUser.Input, GetUser.Output> {

    record Input(UserId userId) { }

    record Output(
        UserId userId,
        String name,
        Email email,
        String firstName,
        String lastName,
        String jobTitle,
        ParticipantId participantId,
        Long createdDate,
        String dfspCode
    ) { }

}
