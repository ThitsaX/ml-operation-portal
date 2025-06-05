package com.thitsaworks.dfsp_portal.participant.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.dfsp_portal.participant.domain.command.CreateLiquidityProfile;
import com.thitsaworks.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateLiquidityProfileBean implements CreateLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateLiquidityProfileBean.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException {

        Optional<Participant> optionalParticipant =
                this.participantRepository.findById(input.getParticipantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        Participant participant = optionalParticipant.get();

        participant.addLiquidityProfile(input.getAccountName(), input.getAccountNumber(),
                input.getCurrency(), input.getIsActive());

        this.participantRepository.save(participant);

        return new Output(true);
    }

}
