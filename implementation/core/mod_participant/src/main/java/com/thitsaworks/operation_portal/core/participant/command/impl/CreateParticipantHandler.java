package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipant;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantAlreadyRegisteredException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateParticipantHandler implements CreateParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws Exception {

        Optional<Participant> participantByDfspCode = this.participantRepository.findByDfspCode(input.dfspCode());

        if (participantByDfspCode.isEmpty()) {

            Participant participant = new Participant(
                    input.dfspCode(),
                    input.name(),
                    input.dfspName(),
                    input.address(),
                    input.mobile());

            this.participantRepository.save(participant);

            return new CreateParticipant.Output(true, participant.getParticipantId());
        }
        else
        {
            throw new ParticipantAlreadyRegisteredException(input.dfspCode().toString());
        }
    }

}
