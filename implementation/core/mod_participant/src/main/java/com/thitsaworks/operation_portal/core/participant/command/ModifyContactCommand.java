package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

public interface ModifyContactCommand {

    record Input(ParticipantId participantId,
                 ContactId contactId,
                 String name,
                 String title,
                 Email email,
                 Mobile mobile,
                 ContactType contactType) {

    }

    record Output(boolean modified,
                  ContactId contactId) {}

    Output execute(Input input) throws ParticipantException;

}
