package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.UserGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface UserGrantCache {
    void save(UserGrantData userGrantData);

    void delete(GrantId grantId);

    Optional<UserGrantData> find(GrantId grantId) throws IAMException;

    UserGrantData get(GrantId grantId) throws IAMException;

    List<UserGrantData> getAll();

    class Updater{

        @PostPersist
        @PostUpdate
        public  void persistOrUpdate(UserGrant userGrant){
            UserGrantCache userGrantCache= SpringContext.getBean(UserGrantCache.class, CacheQualifiers.DEFAULT);

            UserGrantData userGrantData= new UserGrantData(userGrant.getGrantId(),
                                                           userGrant.getUser()
                                                                    .getUserId(),
                                                           userGrant.getIAMAction()
                                                                    .getActionId());
            userGrantCache.save(userGrantData);
        }
        @PostRemove
        public void postRemove(UserGrant userGrant){
            UserGrantCache userGrantCache=SpringContext.getBean(UserGrantCache.class,CacheQualifiers.DEFAULT);

            userGrantCache.delete(userGrant.getGrantId());
        }
    }
}
