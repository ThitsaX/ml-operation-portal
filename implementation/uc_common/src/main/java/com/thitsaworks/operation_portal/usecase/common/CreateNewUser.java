package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface CreateNewUser extends UseCase<CreateNewUser.Input, CreateNewUser.Output> {

    record Input(String name,
                 Email email,
                 ParticipantId participantId) { }

    record Output(boolean created) { }

}
