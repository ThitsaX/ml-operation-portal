package com.thitsaworks.dfsp_portal.participant.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.dfsp_portal.participant.domain.command.CreateContact;
import com.thitsaworks.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateContactBean implements CreateContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateContactBean.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException {

        Optional<Participant> optionalParticipant =
                this.participantRepository.findById(input.getParticipantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        if (input.getContactType().equals("Business")) {
            participant.businessContact(input.getName(), input.getTitle(), input.getEmail(), input.getMobile());
        } else {
            participant.technicalContact(input.getName(), input.getTitle(), input.getEmail(), input.getMobile());
        }

        this.participantRepository.save(participant);

        return new CreateContact.Output(true);

    }

}
