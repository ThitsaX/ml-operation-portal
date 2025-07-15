package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateParticipantNDCCommandHandler implements CreateParticipantNDCCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateParticipantNDCCommandHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        return null;
    }

}
