package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import com.thitsaworks.operation_portal.core.participant.exception.EmailAlreadyRegisteredException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateParticipantUserBean implements CreateParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantBean.class);

    @Autowired
    private ParticipantUserRepository participantUserRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException, EmailAlreadyRegisteredException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.getParticipantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        Optional<ParticipantUser> optionalParticipantUser =
                this.participantUserRepository.findByEmailAndIsDeleted(input.getEmail(),false);

        if (optionalParticipantUser.isPresent()) {
            throw new EmailAlreadyRegisteredException(input.getEmail().getValue());

        }

        ParticipantUser participantUser =
                participant.addUser(input.getName(), input.getEmail(), input.getFirstName(), input.getLastName(),
                        input.getJobTitle());
        
        this.participantRepository.save(participant);

        return new Output(true, participantUser.getParticipantUserId());
    }

}
