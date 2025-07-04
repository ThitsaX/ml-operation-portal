package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContact;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Contact;
import com.thitsaworks.operation_portal.core.participant.model.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyContactHandler implements ModifyContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactHandler.class);

    private final ContactRepository contactRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Contact contact =
                this.contactRepository.findById(input.contactId()).orElseThrow(() -> new ParticipantException(
                        ParticipantErrors.CONTACT_NOT_FOUND));


        this.contactRepository.save(
                contact.name(input.name())
                       .title(input.title())
                       .email(input.email())
                       .mobile(input.mobile())
                        .contactType(input.contactType())
        );

        return new ModifyContact.Output(true, contact.getContactId());
    }

}
