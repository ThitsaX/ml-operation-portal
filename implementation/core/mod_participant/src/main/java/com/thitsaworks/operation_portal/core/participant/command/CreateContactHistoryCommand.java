package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactHistoryId;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface CreateContactHistoryCommand {

    Output execute(Input input) throws ParticipantException;
    record Input(ContactId contactId,
                 ParticipantId participantId){}
    record Output(ContactHistoryId contactHistoryId){}
}
