package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface RemoveUserCommand {

    record Input(ParticipantId participantId, UserId userId) {}

    record Output(UserId userId, boolean removed) {}

    Output execute(Input input) throws ParticipantException;

}
