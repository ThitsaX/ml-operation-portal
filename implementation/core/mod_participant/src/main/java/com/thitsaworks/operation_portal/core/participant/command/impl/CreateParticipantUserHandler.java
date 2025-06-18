package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUser;
import com.thitsaworks.operation_portal.core.participant.exception.EmailAlreadyRegisteredException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateParticipantUserHandler implements CreateParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantHandler.class);

    private final ParticipantUserRepository participantUserRepository;

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException, EmailAlreadyRegisteredException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.participantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        Optional<ParticipantUser> optionalParticipantUser =
                this.participantUserRepository.findByEmailAndIsDeleted(input.email(), false);

        if (optionalParticipantUser.isPresent()) {
            throw new EmailAlreadyRegisteredException(input.email().getValue());

        }

        ParticipantUser participantUser =
                participant.addUser(input.name(), input.email(), input.firstName(), input.lastName(),
                                    input.jobTitle());
        
        this.participantRepository.save(participant);

        return new Output(true, participantUser.getParticipantUserId());
    }

}
