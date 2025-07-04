package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveParticipantUserHandler implements RemoveParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParticipantUserHandler.class);

    private final ParticipantRepository participantRepository;

    private final ParticipantUserRepository participantUserRepository;

    @Override
    @CoreWriteTransactional
    public RemoveParticipantUser.Output execute(RemoveParticipantUser.Input input)
            throws ParticipantException {

        this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.PARTICIPANT_NOT_FOUND));

        ParticipantUser participantUser = this.participantUserRepository.findById(input.participantUserId())
                                                                        .orElseThrow(() -> new ParticipantException(
                                                                                ParticipantErrors.USER_NOT_FOUND));

        this.participantUserRepository.save(participantUser.isDeleted(true));

        return new RemoveParticipantUser.Output(input.participantUserId(), true);
    }

}
