package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveContact extends UseCase<RemoveContact.Input, RemoveContact.Output> {

    record Input(ParticipantId participantId,
                 ContactId contactId) { }

    record Output(boolean removed,
                  ContactId contactId) { }

}
