package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.QLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.repository.LiquidityProfileRepository;
import com.thitsaworks.operation_portal.core.participant.query.LiquidityProfileQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class LiquidityProfileJpaQueryHandler implements LiquidityProfileQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidityProfileJpaQueryHandler.class);

    private final LiquidityProfileRepository liquidityProfileRepository;

    private final QLiquidityProfile liquidityProfile = QLiquidityProfile.liquidityProfile;

    @Override
    public List<LiquidityProfileData> getLiquidityProfiles(ParticipantId participantId) {

        BooleanExpression predicate = this.liquidityProfile.participant.participantId.eq(participantId);

        List<LiquidityProfile> liquidityProfiles = (List<LiquidityProfile>) this.liquidityProfileRepository.findAll(
            predicate);

        return liquidityProfiles.stream()
                                .map(LiquidityProfileData::new)
                                .toList();

    }

    @Override
    public List<LiquidityProfileData> getActiveLiquidityProfiles(ParticipantId participantId) {

        BooleanExpression
            predicate =
            this.liquidityProfile.participant.participantId.eq(participantId)
                                                           .and(this.liquidityProfile.isActive.eq(true));

        List<LiquidityProfile> liquidityProfiles = (List<LiquidityProfile>) this.liquidityProfileRepository.findAll(
            predicate);

        return liquidityProfiles.stream()
                                .map(LiquidityProfileData::new)
                                .toList();
    }

    @Override
    public LiquidityProfileData get(LiquidityProfileId liquidityProfileId) throws ParticipantException {

        BooleanExpression predicate = this.liquidityProfile.liquidityProfileId.eq(liquidityProfileId);

        Optional<LiquidityProfile> optionalLiquidityProfile = this.liquidityProfileRepository.findOne(predicate);

        if (optionalLiquidityProfile.isEmpty()) {

            throw new InputException(ParticipantErrors.LIQUIDITY_PROFILE_NOT_FOUND.format(liquidityProfileId.getId().toString()));
        }

        return new LiquidityProfileData(optionalLiquidityProfile.get());
    }

}
