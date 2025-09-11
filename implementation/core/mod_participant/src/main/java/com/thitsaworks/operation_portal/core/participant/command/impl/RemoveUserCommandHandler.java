package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.RemoveUserCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.User;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveUserCommandHandler implements RemoveUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveUserCommandHandler.class);

    private final ParticipantRepository participantRepository;

    private final UserRepository userRepository;

    @Override
    @CoreWriteTransactional
    public RemoveUserCommand.Output execute(RemoveUserCommand.Input input)
            throws ParticipantException {

        this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.PARTICIPANT_NOT_FOUND
                                                                            .defaultMessage("System cannot find the participant with provided ID. ["
                                                                                    + input.participantId().getId() + "].")));
        User user = this.userRepository.findById(input.userId())
                                       .orElseThrow(() -> new ParticipantException(
                                               ParticipantErrors.USER_NOT_FOUND.defaultMessage(
                                                       "System cannot find the user with provided ID [" + input.userId().getId() + "].")));

        this.userRepository.save(user.isDeleted(true));

        return new RemoveUserCommand.Output(input.userId(), true);
    }

}
