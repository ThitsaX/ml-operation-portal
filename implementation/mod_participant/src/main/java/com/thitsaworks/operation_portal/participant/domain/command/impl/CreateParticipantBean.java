package com.thitsaworks.operation_portal.participant.domain.command.impl;

import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.participant.domain.command.CreateParticipant;
import com.thitsaworks.operation_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.participant.exception.ParticipantAlreadyRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateParticipantBean implements CreateParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantBean.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws Exception {

        Optional<Participant> participantByDfspCode = this.participantRepository.findByDfspCode(input.getDfspCode());

        if (participantByDfspCode.isEmpty()) {

            Participant participant = new Participant(
                    input.getDfspCode(),
                    input.getName(),
                    input.getDfspName(),
                    input.getAddress(),
                    input.getMobile());

            this.participantRepository.save(participant);

            return new CreateParticipant.Output(true, participant.getParticipantId());
        }
        else
        {
           throw new ParticipantAlreadyRegisteredException(input.getDfspCode().toString()) ;
        }
    }

}
