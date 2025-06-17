package com.thitsaworks.operation_portal.core.iam.query.cache;

import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface PrincipalCache {

    void save(PrincipalData principalData);

    PrincipalData get(AccessKey accessKey);

    PrincipalData get(PrincipalId principalId);

    PrincipalData get(AccessKey accessKey, RealmType realmType);

    void delete(AccessKey accessKey);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Principal principal) {

            PrincipalCache participantCache = com.thitsaworks.operation_portal.component.spring.SpringContext.getBean(
                    PrincipalCache.class,
                    CacheQualifiers.DEFAULT);

            PrincipalData participantUserData = new PrincipalData(principal);

            participantCache.save(participantUserData);
        }

        @PostRemove
        public void postRemove(Principal principal) {

            PrincipalCache principalCache = com.thitsaworks.operation_portal.component.spring.SpringContext.getBean(
                    PrincipalCache.class,
                    CacheQualifiers.DEFAULT);
            principalCache.delete(principal.getAccessKey());

        }

    }


}
