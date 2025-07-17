package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface RemoveParticipantUserCommand {

    record Input(ParticipantId participantId,
                 ParticipantUserId participantUserId) {}

    record Output(
            ParticipantUserId participantUserId, boolean removed) {}

    Output execute(Input input) throws ParticipantException;

}
