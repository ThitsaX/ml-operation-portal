package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.data.ContactData;
import com.thitsaworks.operation_portal.core.participant.exception.ContactNotFoundException;

import java.util.List;

public interface ContactQuery {

    List<ContactData> getContacts(ParticipantId participantId);

    ContactData get(ContactId contactId) throws ContactNotFoundException;

}

