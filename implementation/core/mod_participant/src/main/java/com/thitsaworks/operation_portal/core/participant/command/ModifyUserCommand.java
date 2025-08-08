package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyUserCommand {

    record Input(
            UserId userId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId) {}

    record Output(UserId userId, boolean modified) {}

    ModifyUserCommand.Output execute(ModifyUserCommand.Input input)
            throws ParticipantException;
}
