package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface CreateUserCommand {

    record Input(String name,
                 Email email,
                 ParticipantId participantId,
                 String firstName,
                 String lastName,
                 String jobTitle) {}

    record Output(boolean created,
                  UserId userId) {}

    Output execute(Input input)
            throws ParticipantException;

}
