package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUserCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
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
public class CreateParticipantUserCommandHandler implements CreateParticipantUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantCommandHandler.class);

    private final ParticipantUserRepository participantUserRepository;

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Participant participant = this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.PARTICIPANT_NOT_FOUND));

        Optional<ParticipantUser> optionalParticipantUser =
                this.participantUserRepository.findByEmailAndIsDeleted(input.email(), false);

        if (optionalParticipantUser.isPresent()) {
            throw new ParticipantException(ParticipantErrors.EMAIL_ALREADY_REGISTERED);

        }

        ParticipantUser participantUser =
                participant.addUser(input.name(), input.email(), input.firstName(), input.lastName(),
                                    input.jobTitle());
        
        this.participantRepository.save(participant);

        return new Output(true, participantUser.getParticipantUserId());
    }

}
