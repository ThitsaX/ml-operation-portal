package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.type.Mobile;

public interface CreateParticipant {

    record Input(String name,
                 DfspCode dfspCode,
                 String dfspName,
                 String address,
                 Mobile mobile) {}

    record Output(boolean created, ParticipantId participantId) {}

    Output execute(Input input) throws Exception;

}
