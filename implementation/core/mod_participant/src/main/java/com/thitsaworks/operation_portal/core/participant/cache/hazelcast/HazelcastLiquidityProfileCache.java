package com.thitsaworks.operation_portal.core.participant.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.LiquidityProfileCache;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier(CacheQualifiers.HAZELCAST)
public class HazelcastLiquidityProfileCache implements LiquidityProfileCache{

    private static final String HZ_LIQUIDITY_PROFILE = "hz_liquidity_profile";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(LiquidityProfileData liquidityProfileData) {

        IMap<Long, LiquidityProfileData> liquidityProfileDataIMap = this.hazelcastInstance.getMap(HZ_LIQUIDITY_PROFILE);
        liquidityProfileDataIMap.put(liquidityProfileData.liquidityProfileId().getId(), liquidityProfileData);

    }

    @Override
    public LiquidityProfileData get(LiquidityProfileId liquidityProfileId) {

        IMap<Long, LiquidityProfileData> liquidityProfileDataIMap = this.hazelcastInstance.getMap(HZ_LIQUIDITY_PROFILE);
        return liquidityProfileDataIMap.get(liquidityProfileId.getId());
    }

    @Override
    public void delete(LiquidityProfileId liquidityProfileId) {

        IMap<Long, LiquidityProfileData> liquidityProfileDataIMap = this.hazelcastInstance.getMap(HZ_LIQUIDITY_PROFILE);
        liquidityProfileDataIMap.delete(liquidityProfileId.getId());
    }

}
