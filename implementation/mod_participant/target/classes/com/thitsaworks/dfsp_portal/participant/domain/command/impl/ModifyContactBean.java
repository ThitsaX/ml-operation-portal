package com.thitsaworks.dfsp_portal.participant.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.participant.domain.Contact;
import com.thitsaworks.dfsp_portal.participant.domain.command.ModifyContact;
import com.thitsaworks.dfsp_portal.participant.domain.repository.ContactRepository;
import com.thitsaworks.dfsp_portal.participant.exception.ContactNotFoundException;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyContactBean implements ModifyContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactBean.class);

    @Autowired
    private ContactRepository contactRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException, ContactNotFoundException {

        Optional<Contact> optionalContact = this.contactRepository.findById(input.getContactId());

        if (optionalContact.isEmpty()) {

            throw new ContactNotFoundException(input.getContactId().getId().toString());
        }

        Contact contact = optionalContact.get();

        this.contactRepository.save(
                contact.name(input.getName())
                       .title(input.getTitle())
                       .email(input.getEmail())
                       .mobile(input.getMobile())
        );

        return new ModifyContact.Output(true, contact.getContactId());
    }

}
