package com.thitsaworks.operation_portal.core.participant.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.LiquidityProfileCache;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.LiquidityProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@Qualifier(CacheQualifiers.PROXY)
public class ProxyLiquidityProfileCache implements LiquidityProfileCache {

    @Autowired
    private LiquidityProfileRepository liquidityProfileRepository;

    @Autowired
    @Qualifier(CacheQualifiers.HAZELCAST)
    private LiquidityProfileCache liquidityProfileCache;

    @Override
    public void save(LiquidityProfileData liquidityProfileData) {

        this.liquidityProfileCache.save(liquidityProfileData);
    }

    @Override
    public LiquidityProfileData get(LiquidityProfileId liquidityProfileId) {

        LiquidityProfileData liquidityProfileData = this.liquidityProfileCache.get(liquidityProfileId);

        if (liquidityProfileData == null) {

            Optional<LiquidityProfile> optionalLiquidityProfile = this.liquidityProfileRepository.findById(liquidityProfileId);

            if (optionalLiquidityProfile.isEmpty()) {

                return null;
            }


            liquidityProfileData = new LiquidityProfileData(optionalLiquidityProfile.get());

            this.liquidityProfileCache.save(liquidityProfileData);
        }

        return liquidityProfileData;
    }


    @Override
    public void delete(LiquidityProfileId liquidityProfileId) {

        this.liquidityProfileCache.delete(liquidityProfileId);

    }

}
