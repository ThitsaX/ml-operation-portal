package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.DeleteParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteParticipantNDCCommandHandler implements DeleteParticipantNDCCommand {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteParticipantNDCCommandHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        Optional<ParticipantNDC> optionalParticipantNDC =
                this.participantNDCRepository.findById(input.participantNDCId());

        boolean removed = false;

        if (!optionalParticipantNDC.isEmpty()) {

            this.participantNDCRepository.delete(optionalParticipantNDC.get());
            removed = true;
        }

        return new Output(removed);
    }

}
