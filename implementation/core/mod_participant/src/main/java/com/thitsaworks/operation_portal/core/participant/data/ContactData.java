package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.model.Contact;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;

public record ContactData(ContactId contactId,
                          ParticipantId participantId,

                          String name,

                          String title,

                          Email email,

                          Mobile mobile,

                          ContactType contactType) {

    public ContactData(Contact contact) {

        this(contact.getContactId(),

             contact.getParticipant().getParticipantId(),

             contact.getName(),

             contact.getTitle(),

             contact.getEmail(),

             contact.getMobile(),

             contact.getParticipant().getBusinessContact() != null &&
                     contact.getParticipant().getBusinessContact().getContactId() == contact.getContactId() ?
                     ContactType.BUSINESS : contact.getParticipant().getTechnicalContact() != null &&
                     contact.getParticipant().getTechnicalContact().getContactId() == contact.getContactId() ?
                     ContactType.Technical : null);

    }

}
