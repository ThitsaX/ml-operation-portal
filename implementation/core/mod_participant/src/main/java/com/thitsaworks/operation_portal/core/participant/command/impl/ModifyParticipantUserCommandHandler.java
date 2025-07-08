package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUserCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyParticipantUserCommandHandler implements ModifyParticipantUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantUserCommandHandler.class);

    private final ParticipantUserRepository participantUserRepository;

    @Override
    @CoreWriteTransactional
    public ModifyParticipantUserCommand.Output execute(Input input) throws ParticipantException {

        ParticipantUser participantUser = this.participantUserRepository.findById(input.participantUserId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.USER_NOT_FOUND));

        this.participantUserRepository.save(
                participantUser.name(input.name())
                               .firstName(input.firstName())
                               .lastName(input.lastName())
                               .jobTitle(input.jobTitle())
        );

        return new ModifyParticipantUserCommand.Output(participantUser.getParticipantUserId(), true);
    }

}

