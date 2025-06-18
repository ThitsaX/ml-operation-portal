package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUser;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyParticipantUserHandler implements ModifyParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantUserHandler.class);

    private final ParticipantUserRepository participantUserRepository;

    @Override
    @CoreWriteTransactional
    public ModifyParticipantUser.Output execute(Input input) throws ParticipantUserNotFoundException {

        Optional<ParticipantUser> optionalUser = this.participantUserRepository.findById(input.participantUserId());

        if (optionalUser.isEmpty()) {

            throw new ParticipantUserNotFoundException(input.participantUserId().getId().toString());

        }

        ParticipantUser participantUser = optionalUser.get();

        this.participantUserRepository.save(
                participantUser.name(input.name())
                               .firstName(input.firstName())
                               .lastName(input.lastName())
                               .jobTitle(input.jobTitle())
        );

        return new ModifyParticipantUser.Output(participantUser.getParticipantUserId(), true);
    }

}

