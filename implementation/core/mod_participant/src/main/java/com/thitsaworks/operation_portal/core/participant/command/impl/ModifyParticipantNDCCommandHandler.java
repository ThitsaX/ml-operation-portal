package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyParticipantNDCCommandHandler implements ModifyParticipantNDCCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantNDCCommandHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantNDCException {

        ParticipantNDC
                existingParticipantNDC = this.participantNDCRepository.findById(input.participantNDCId())
                                                                      .orElseThrow(() -> new ParticipantNDCException(
                                                                              ParticipantErrors.PARTICIPANT_NDC_NOT_FOUND.format(input.participantNDCId().getId().toString())));

        existingParticipantNDC.moveParticipantNDCToHistory(existingParticipantNDC);

        this.participantNDCRepository.save(
                existingParticipantNDC.ndcPercent(input.ndcPercent()));

        return new Output(existingParticipantNDC.getParticipantNDCId());
    }

}
