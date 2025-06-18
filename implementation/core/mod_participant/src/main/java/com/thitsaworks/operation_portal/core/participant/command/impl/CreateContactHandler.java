package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateContactHandler implements CreateContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateContactHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException {

        Optional<Participant> optionalParticipant =
                this.participantRepository.findById(input.participantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        if (input.contactType().equals("Business")) {
            participant.businessContact(input.name(), input.title(), input.email(), input.mobile());
        } else {
            participant.technicalContact(input.name(), input.title(), input.email(), input.mobile());
        }

        this.participantRepository.save(participant);

        return new CreateContact.Output(true);

    }

}
