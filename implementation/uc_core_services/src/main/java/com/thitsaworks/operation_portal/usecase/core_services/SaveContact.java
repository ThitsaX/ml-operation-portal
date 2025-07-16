package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;

public interface SaveContact extends UseCase<SaveContact.Input, SaveContact.Output> {

    record Input(ParticipantId participantId,
                 ContactId contactId,
                 String name,
                 String title,
                 Email email,
                 Mobile mobile,
                 ContactType contactType) { }

    record Output(boolean saved,
                  ContactId contactId) { }

}
