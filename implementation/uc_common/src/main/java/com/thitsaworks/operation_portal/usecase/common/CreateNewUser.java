package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class CreateNewUser
        extends AbstractAuditableUseCase<CreateNewUser.Input, CreateNewUser.Output> {

    public record Input(String name,
                 Email email,
                 ParticipantId participantId) {



    }

    public record Output(
            boolean created) {


    }

}
