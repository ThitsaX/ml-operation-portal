package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.UserRoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface UserRoleCache {

    void save(UserRoleData userRoleData);

    void delete(UserRoleId userRoleId);

    Optional<UserRoleData> find(UserRoleId userRoleId) throws IAMException;

    UserRoleData get(UserRoleId userRoleId) throws IAMException;

    List<UserRoleData> getAll();

    class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(UserRole userRole) {

            UserRoleCache uerRoleCache = SpringContext.getBean(UserRoleCache.class, CacheQualifiers.DEFAULT);
            UserRoleData userRoleData = new UserRoleData(userRole.getUserRoleId(),
                                                         userRole.getRole()
                                                                 .getRoleId(),
                                                         userRole.getUser()
                                                                 .getUserId());
            uerRoleCache.save(userRoleData);
        }

        @PostRemove
        public void postRemove(UserRole userRole) {

            UserRoleCache uerRoleCache = SpringContext.getBean(UserRoleCache.class, CacheQualifiers.DEFAULT);

            uerRoleCache.delete(userRole.getUserRoleId());
        }

    }

}