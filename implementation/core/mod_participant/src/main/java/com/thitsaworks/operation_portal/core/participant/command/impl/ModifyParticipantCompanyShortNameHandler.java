package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCompanyShortName;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyParticipantCompanyShortNameHandler implements ModifyParticipantCompanyShortName {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantCompanyShortNameHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.participantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());

        }

        Participant participant = optionalParticipant.get();

        this.participantRepository.save(
                participant
                        .dfsp_name(input.companyShortName())

        );

        return new Output(true, participant.getParticipantId());
    }

}
