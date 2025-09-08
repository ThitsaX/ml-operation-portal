package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyUserCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.User;
import com.thitsaworks.operation_portal.core.participant.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyUserCommandHandler implements ModifyUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserCommandHandler.class);

    private final UserRepository userRepository;

    @Override
    @CoreWriteTransactional
    public ModifyUserCommand.Output execute(Input input) throws ParticipantException {

        User user = this.userRepository.findById(input.userId())
                                       .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.USER_NOT_FOUND));

        this.userRepository.save(
                user.name(input.name())
                    .firstName(input.firstName())
                    .lastName(input.lastName())
                    .jobTitle(input.jobTitle())
                                );

        return new ModifyUserCommand.Output(user.getUserId(), true);
    }

}

