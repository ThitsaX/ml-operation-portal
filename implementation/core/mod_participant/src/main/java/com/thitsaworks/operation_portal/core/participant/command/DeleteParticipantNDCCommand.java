package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;

public interface DeleteParticipantNDCCommand {

    record Input(ParticipantNDCId participantNDCId) {}

    record Output(boolean removed) {}

    Output execute(Input input);

}
