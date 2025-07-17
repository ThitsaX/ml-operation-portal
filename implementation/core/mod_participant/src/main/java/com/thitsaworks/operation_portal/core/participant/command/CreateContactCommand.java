package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface CreateContactCommand {

    record Input(ParticipantId participantId,
                 String name,
                 String position,
                 Email email,
                 Mobile mobile,
                 ContactType contactType) { }

    record Output(boolean created,
                  ContactId contactId) { }

    Output execute(Input input) throws ParticipantException;

}
