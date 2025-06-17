package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.repository.LiquidityProfileRepository;
import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
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
    @DfspWriteTransactional
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
