package com.thitsaworks.operation_portal.core.iam.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier(CacheQualifiers.HAZELCAST)
public class HazelcastPrincipalCache implements PrincipalCache {

    private static final String HZ_PRINCIPAL = "hz_principal";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(PrincipalData principalData) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        principalDataIMap.put(principalData.accessKey().getId(), principalData);
    }

    @Override
    public PrincipalData get(AccessKey accessKey) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        return principalDataIMap.get(accessKey.getId());
    }

    @Override
    public PrincipalData get(PrincipalId principalId) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        return principalDataIMap.get(principalId.getId());
    }

    @Override
    public PrincipalData get(AccessKey accessKey, RealmType realmType) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        PrincipalData principalData = principalDataIMap.get(accessKey.getId());

        if (principalData != null && principalData.realmType().equals(realmType)) {
            return principalDataIMap.get(accessKey.getId());
        }

        return null;

    }

    @Override
    public void delete(AccessKey accessKey) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        principalDataIMap.delete(accessKey.getId());
    }

}
