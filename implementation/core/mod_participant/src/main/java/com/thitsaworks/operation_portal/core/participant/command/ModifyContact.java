package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ContactNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

public interface ModifyContact {

    record Input(ParticipantId participantId,
                 ContactId contactId,
                 String name,
                 String title,
                 Email email,
                 Mobile mobile) {}

    record Output(boolean modified,
                  ContactId contactId) {}

    Output execute(Input input) throws ParticipantNotFoundException, ContactNotFoundException;

}
