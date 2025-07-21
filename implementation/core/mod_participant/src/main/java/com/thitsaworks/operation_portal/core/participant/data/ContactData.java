package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.model.Contact;

public record ContactData(ContactId contactId,

                          ParticipantId participantId,

                          String name,

                          String position,

                          Email email,

                          Mobile mobile,

                          ContactType contactType) {

    public ContactData(Contact contact) {

        this(contact.getContactId(),

             contact.getParticipant().getParticipantId(),

             contact.getName(),

             contact.getPosition(),

             contact.getEmail(),

             contact.getMobile(),

             contact.getContactType());

    }

}
