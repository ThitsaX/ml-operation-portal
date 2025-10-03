package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantStatusCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ModifyParticipantStatusCommandHandler implements ModifyParticipantStatusCommand {

    private static  final Logger logger = LoggerFactory.getLogger(ModifyParticipantStatusCommandHandler.class);
    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var participant = this.participantRepository.findByParticipantName(input.participantName())
                                                        .orElseThrow(() -> new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                                                                .format(input.participantName().toString())));

       this.participantRepository.save(participant.status(input.participantStatus()));

       return new Output(true,participant.getParticipantId());

    }

}
