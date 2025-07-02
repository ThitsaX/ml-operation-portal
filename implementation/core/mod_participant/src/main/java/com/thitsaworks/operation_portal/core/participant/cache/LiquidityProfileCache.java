package com.thitsaworks.operation_portal.core.participant.cache;

import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface LiquidityProfileCache {

    void save(LiquidityProfileData liquidityProfileData);

    LiquidityProfileData get(LiquidityProfileId liquidityProfileId);

    void delete(LiquidityProfileId liquidityProfileId);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(LiquidityProfile liquidityProfile) {

            LiquidityProfileCache participantCache = SpringContext.getBean(LiquidityProfileCache.class, CacheQualifiers.DEFAULT);

            LiquidityProfileData liquidityProfileData = new LiquidityProfileData(liquidityProfile);

            participantCache.save(liquidityProfileData);
        }

        @PostRemove
        public void postRemove(LiquidityProfile liquidityProfile) {

            LiquidityProfileCache participantCache = SpringContext.getBean(LiquidityProfileCache.class, CacheQualifiers.DEFAULT);
            participantCache.delete(liquidityProfile.getLiquidityProfileId());

        }

    }

}
