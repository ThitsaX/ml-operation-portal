package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateLiquidityProfileCommandHandler implements CreateLiquidityProfileCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateLiquidityProfileCommandHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Participant participant = this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                                                                    .format(input.participantId().getId())));

        var liquidityProfile = participant.addLiquidityProfile(input.bankName(),
                                                               input.accountName(),
                                                               input.accountNumber(),
                                                               input.currency(),
                                                               input.isActive());

        this.participantRepository.save(participant);

        return new Output(true, liquidityProfile.getLiquidityProfileId());
    }

}
