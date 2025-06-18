package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.repository.LiquidityProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModifyLiquidityProfileHandler implements ModifyLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileHandler.class);

    private final LiquidityProfileRepository liquidityProfileRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException, LiquidityProfileNotFoundException {

        Optional<LiquidityProfile> optionalLiquidityProfile =
                this.liquidityProfileRepository.findById(input.liquidityProfileId());

        if (optionalLiquidityProfile.isEmpty()) {

            throw new LiquidityProfileNotFoundException(input.liquidityProfileId().getId().toString());
        }

        LiquidityProfile liquidityProfile = optionalLiquidityProfile.get();

        this.liquidityProfileRepository.save(
                liquidityProfile.accountName(input.accountName()).accountNumber(input.accountNumber())
                                .currency(input.currency()).isActive(input.isActive()));

        return new Output(liquidityProfile.getLiquidityProfileId(), true);
    }

}
