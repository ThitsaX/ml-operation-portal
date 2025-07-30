package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface RoleGrantCache {

    void save(RoleGrantData roleGrantData);

    void delete(GrantId grantId);

    Optional<RoleGrantData> find(GrantId grantId) throws IAMException;

    RoleGrantData get(GrantId grantId) throws IAMException;

    List<RoleGrantData> getAll();

    class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(RoleGrant roleGrant) {

            RoleGrantCache roleGrantCache = SpringContext.getBean(RoleGrantCache.class, CacheQualifiers.DEFAULT);

            RoleGrantData roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                                            roleGrant.getRole()
                                                                     .getRoleId(),
                                                            roleGrant.getIAMAction()
                                                                     .getActionId());
            roleGrantCache.save(roleGrantData);

        }

        @PostRemove
        public void postRemove(RoleGrant roleGrant) {

            RoleGrantCache roleGrantCache = SpringContext.getBean(RoleGrantCache.class, CacheQualifiers.DEFAULT);
            roleGrantCache.delete(roleGrant.getGrantId());

        }

    }

}