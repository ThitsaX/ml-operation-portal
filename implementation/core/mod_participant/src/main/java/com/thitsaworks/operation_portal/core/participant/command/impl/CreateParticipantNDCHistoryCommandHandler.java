package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDCHistory;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateParticipantNDCHistoryCommandHandler implements CreateParticipantNDCHistoryCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantNDCHistoryCommandHandler.class);

    private final ParticipantNDCHistoryRepository participantNDCHistoryRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        ParticipantNDCHistory participantNDCHistory = new ParticipantNDCHistory(input.participantNDC(),
                                                                                input.dfspCode(),
                                                                                input.currency(),
                                                                                input.ndcPercent(),
                                                                                input.ndcAmount());

        this.participantNDCHistoryRepository.save(participantNDCHistory);

        return new Output(participantNDCHistory.getParticipantNDCHistoryId());
    }

}
