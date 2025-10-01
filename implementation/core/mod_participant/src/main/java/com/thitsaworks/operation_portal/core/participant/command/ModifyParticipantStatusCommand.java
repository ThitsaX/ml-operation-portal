package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyParticipantStatusCommand {

    record Input(ParticipantName participantName,
                 ParticipantStatus participantStatus) { }

    record Output(boolean modified,
                  ParticipantId participantId) { }

    Output execute(Input input) throws ParticipantException;

}
