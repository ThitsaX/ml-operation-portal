package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
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
public class RemoveParticipantUserHandler implements RemoveParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParticipantUserHandler.class);

    private final ParticipantRepository participantRepository;

    private final ParticipantUserRepository participantUserRepository;

    @Override
    @CoreWriteTransactional
    public RemoveParticipantUser.Output execute(RemoveParticipantUser.Input input)
            throws ParticipantNotFoundException, ParticipantUserNotFoundException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.participantId());

        if (!optionalParticipant.isPresent()) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());
        }

        Optional<ParticipantUser> optionalParticipantUser =
                this.participantUserRepository.findById(input.participantUserId());

        if (!optionalParticipantUser.isPresent()) {

            throw new ParticipantUserNotFoundException(input.participantUserId().getId().toString());
        }

        ParticipantUser participantUser = optionalParticipantUser.get();

        this.participantUserRepository.save(participantUser.isDeleted(true));

        return new RemoveParticipantUser.Output(input.participantUserId(), true);
    }

}
