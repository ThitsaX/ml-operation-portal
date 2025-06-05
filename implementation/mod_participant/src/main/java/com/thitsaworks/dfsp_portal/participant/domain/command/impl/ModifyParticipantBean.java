package com.thitsaworks.dfsp_portal.participant.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.dfsp_portal.participant.domain.command.ModifyParticipant;
import com.thitsaworks.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
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
    @WriteTransactional
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
