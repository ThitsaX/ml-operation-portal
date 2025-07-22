package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyParticipantCommand {

    record Input(ParticipantId participantId,
                 String description,
                 String address,
                 Mobile mobile,
                 String logoDataType,
                 byte[] logo) { }

    record Output(boolean modified,
                  ParticipantId participantId) { }

    Output execute(Input input) throws ParticipantException;

}
