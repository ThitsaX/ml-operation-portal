package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

public interface CreateContact {

    record Input(String name, String title, Email email, Mobile mobile, ParticipantId participantId,
                 String contactType) {}

    record Output(boolean created) {}

    Output execute(Input input) throws ParticipantNotFoundException;
}
