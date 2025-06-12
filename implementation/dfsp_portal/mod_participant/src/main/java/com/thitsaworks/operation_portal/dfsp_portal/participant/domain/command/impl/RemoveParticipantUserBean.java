package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.ParticipantUser;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.repository.ParticipantUserRepository;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoveParticipantUserBean implements RemoveParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParticipantUserBean.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantUserRepository participantUserRepository;

    @Override
    @DfspWriteTransactional
    public RemoveParticipantUser.Output execute(RemoveParticipantUser.Input input)
            throws ParticipantNotFoundException, ParticipantUserNotFoundException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.getParticipantId());

        if (!optionalParticipant.isPresent()) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        Optional<ParticipantUser> optionalParticipantUser =
                this.participantUserRepository.findById(input.getParticipantUserId());

        if (!optionalParticipantUser.isPresent()) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        ParticipantUser participantUser = optionalParticipantUser.get();

        this.participantUserRepository.save(participantUser.isDeleted(true));

        return new RemoveParticipantUser.Output(input.getParticipantUserId(), true);
    }

}
