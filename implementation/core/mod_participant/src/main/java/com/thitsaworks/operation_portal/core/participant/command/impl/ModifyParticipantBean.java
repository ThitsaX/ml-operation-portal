package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipant;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyParticipantBean implements ModifyParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantBean.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.getParticipantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());

        }

        Participant participant = optionalParticipant.get();

        this.participantRepository.save(
                participant
                        .name(input.getCompanyName())
                        .address(input.getAddress())
                           .mobile(input.getMobile())
        );

        return new ModifyParticipant.Output(true, participant.getParticipantId());
    }

}
