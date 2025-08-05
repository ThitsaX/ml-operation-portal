package com.thitsaworks.operation_portal.core.iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface PrincipalCache {

    void save(PrincipalData principalData);

    PrincipalData get(AccessKey accessKey);

    PrincipalData get(PrincipalId principalId);

    void delete(AccessKey accessKey);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Principal principal) {

            PrincipalCache principalCache = SpringContext.getBean(
                    PrincipalCache.class,
                    CacheQualifiers.DEFAULT);

            PrincipalData participantUserData = new PrincipalData(principal);

            principalCache.save(participantUserData);
        }

        @PostRemove
        public void postRemove(Principal principal) {

            PrincipalCache principalCache = SpringContext.getBean(
                    PrincipalCache.class,
                    CacheQualifiers.DEFAULT);
            principalCache.delete(principal.getAccessKey());

        }

    }


}
