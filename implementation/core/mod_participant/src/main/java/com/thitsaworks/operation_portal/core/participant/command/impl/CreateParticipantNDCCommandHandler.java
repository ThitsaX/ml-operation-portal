package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateParticipantNDCCommandHandler implements CreateParticipantNDCCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantNDCCommandHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Participant participant = this.participantRepository.findByParticipantName(new ParticipantName(input.participantName()))
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.PARTICIPANT_NOT_FOUND.format(input.participantName())));

        ParticipantNDC participantNDC = new ParticipantNDC(input.participantName(),
                                                           input.currency(),
                                                           input.ndcPercent());

        this.participantNDCRepository.save(participantNDC);

        return new Output(participantNDC.getParticipantNDCId());
    }

}
