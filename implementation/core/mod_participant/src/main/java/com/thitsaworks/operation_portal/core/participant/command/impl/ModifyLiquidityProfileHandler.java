package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.repository.LiquidityProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyLiquidityProfileHandler implements ModifyLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileHandler.class);

    private final LiquidityProfileRepository liquidityProfileRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        LiquidityProfile liquidityProfile =
                this.liquidityProfileRepository.findById(input.liquidityProfileId())
                                               .orElseThrow(() -> new ParticipantException(
                                                       ParticipantErrors.LIQUIDITY_PROFILE_NOT_FOUND));



        this.liquidityProfileRepository.save(
                liquidityProfile.accountName(input.accountName()).accountNumber(input.accountNumber())
                                .currency(input.currency()).isActive(input.isActive()));

        return new Output(liquidityProfile.getLiquidityProfileId(), true);
    }

}
