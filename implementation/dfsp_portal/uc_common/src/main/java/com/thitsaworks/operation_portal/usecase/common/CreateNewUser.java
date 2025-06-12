package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class CreateNewUser
        extends AbstractAuditableUseCase<CreateNewUser.Input, CreateNewUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String name;

        private Email email;

        private ParticipantId participantId;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private boolean created;

    }

}
