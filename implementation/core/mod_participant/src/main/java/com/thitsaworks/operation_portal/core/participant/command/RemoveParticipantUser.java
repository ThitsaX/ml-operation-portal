package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface RemoveParticipantUser {

    record Input(ParticipantId participantId,
                 ParticipantUserId participantUserId) {}

    record Output(
            ParticipantUserId participantUserId, boolean removed) {}

    RemoveParticipantUser.Output execute(RemoveParticipantUser.Input input) throws ParticipantException;

}
