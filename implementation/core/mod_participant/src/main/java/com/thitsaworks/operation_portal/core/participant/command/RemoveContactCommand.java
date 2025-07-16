package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface RemoveContactCommand {

    Output execute(Input input) throws ParticipantException;

    record Input(ParticipantId participantId,
                 ContactId contactId) { }

    record Output(boolean removed,
                  ContactId contactId) { }

}
