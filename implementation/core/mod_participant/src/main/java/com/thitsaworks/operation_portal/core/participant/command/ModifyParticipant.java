package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

public interface ModifyParticipant {

    record Input(ParticipantId participantId,
                 String companyName,
                 String address, Mobile mobile) {}

    record Output(boolean modified,
                  ParticipantId participantId) {}

    Output execute(Input input) throws ParticipantNotFoundException;

}
