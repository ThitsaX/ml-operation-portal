package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface CreateParticipantUserCommand {

    record Input(String name,
                 Email email,
                 ParticipantId participantId,
                 String firstName,
                 String lastName,
                 String jobTitle) {}

    record Output(boolean created,
                  ParticipantUserId participantUserId) {}

    Output execute(Input input)
            throws ParticipantException;

}
