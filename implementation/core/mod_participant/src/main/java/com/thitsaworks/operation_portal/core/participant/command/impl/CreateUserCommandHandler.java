package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateUserCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.User;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CreateUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateUserCommandHandler.class);

    private final UserRepository userRepository;

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Participant participant = this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                ParticipantErrors.PARTICIPANT_NOT_FOUND
                                                                    .format(input.participantId().getId())));

        Optional<User> optionalUser =
            this.userRepository.findByEmailAndIsDeleted(input.email(), false);

        if (optionalUser.isPresent()) {
            throw new ParticipantException(ParticipantErrors.EMAIL_ALREADY_REGISTERED.format(input.email()
                                                                                                  .getValue()));

        }

        User user =
            participant.addUser(input.name(), input.email(), input.firstName(), input.lastName(),
                                input.jobTitle());

        this.participantRepository.save(participant);

        return new Output(true, user.getUserId());
    }

}
