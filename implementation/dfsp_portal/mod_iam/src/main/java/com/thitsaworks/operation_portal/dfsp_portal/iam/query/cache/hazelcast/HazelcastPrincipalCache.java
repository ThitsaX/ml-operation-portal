package com.thitsaworks.operation_portal.dfsp_portal.iam.query.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.RealmType;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier(PrincipalCache.Strategies.HAZELCAST)
public class HazelcastPrincipalCache implements PrincipalCache {

    private static final String HZ_PRINCIPAL = "hz_principal";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(PrincipalData principalData) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        principalDataIMap.put(principalData.getAccessKey().getId(), principalData);
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

        if (principalData != null && principalData.getRealmType().equals(realmType)) {
            return principalDataIMap.get(accessKey.getId());
        }

        return null;

    }

    @Override
    public void delete(AccessKey accessKey) {

        IMap<Long, PrincipalData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PRINCIPAL);
        principalDataIMap.delete(accessKey.getId());
    }

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Principal principal) {

            PrincipalCache principalCache =
                    SpringContext.getBean(PrincipalCache.class, PrincipalCache.Strategies.HAZELCAST);
            PrincipalData principalData = new PrincipalData(principal);
            principalCache.save(principalData);
        }

        @PostRemove
        public void postRemove(Principal principal) {

            PrincipalCache principalCache =
                    SpringContext.getBean(PrincipalCache.class, PrincipalCache.Strategies.HAZELCAST);
            principalCache.delete(principal.getAccessKey());
        }

    }

}
