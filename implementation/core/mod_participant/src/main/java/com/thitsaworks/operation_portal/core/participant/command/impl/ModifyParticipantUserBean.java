package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyParticipantUserBean implements ModifyParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantUserBean.class);

    @Autowired
    private ParticipantUserRepository participantUserRepository;

    @Override
    @DfspWriteTransactional
    public ModifyParticipantUser.Output execute(Input input) throws ParticipantUserNotFoundException {

        Optional<ParticipantUser> optionalUser = this.participantUserRepository.findById(input.getParticipantUserId());

        if (optionalUser.isEmpty()) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());

        }

        ParticipantUser participantUser = optionalUser.get();

        this.participantUserRepository.save(
                participantUser.name(input.getName())
                               .firstName(input.getFirstName())
                               .lastName(input.getLastName())
                               .jobTitle(input.getJobTitle())
        );

        return new ModifyParticipantUser.Output(participantUser.getParticipantUserId(), true);
    }

}

