package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyLiquidityProfileCommandHandler implements ModifyLiquidityProfileCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileCommandHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var
            participant =
            this.participantRepository.findById(input.participantId())
                                      .orElseThrow(() -> new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND));

        var liquidityProfile = participant.updateLiquidityProfile(input.liquidityProfileId(),
                                                                  input.bankName(),
                                                                  input.accountName(),
                                                                  input.accountNumber(),
                                                                  input.currency());

        this.participantRepository.saveAndFlush(participant);

        return new Output(true, liquidityProfile.getLiquidityProfileId());
    }

}
