package com.thitsaworks.operation_portal.participant.domain.command.impl;

import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.participant.domain.LiquidityProfile;
import com.thitsaworks.operation_portal.participant.domain.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.participant.domain.repository.LiquidityProfileRepository;
import com.thitsaworks.operation_portal.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModifyLiquidityProfileBean implements ModifyLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileBean.class);

    @Autowired
    private LiquidityProfileRepository liquidityProfileRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws ParticipantNotFoundException, LiquidityProfileNotFoundException {

        Optional<LiquidityProfile> optionalLiquidityProfile =
                this.liquidityProfileRepository.findById(input.getLiquidityProfileId());

        if (optionalLiquidityProfile.isEmpty()) {

            throw new LiquidityProfileNotFoundException(input.getLiquidityProfileId().getId().toString());
        }

        LiquidityProfile liquidityProfile = optionalLiquidityProfile.get();

        this.liquidityProfileRepository.save(
                liquidityProfile.accountName(input.getAccountName()).accountNumber(input.getAccountNumber())
                                .currency(input.getCurrency()).isActive(input.getIsActive()));

        return new Output(liquidityProfile.getLiquidityProfileId(), true);
    }

}
