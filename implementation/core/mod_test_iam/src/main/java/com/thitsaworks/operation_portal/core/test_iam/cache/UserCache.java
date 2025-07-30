package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface UserCache {

    void save(UserData userData);
    void delete(UserId id);

    Optional<UserData> find(AccessKey accessKey) throws IAMException;

    UserData get(UserId id) throws IAMException;

    UserData get(AccessKey accessKey, RealmType realmType) throws IAMException;

    List<UserData> getAll();

    void delete(AccessKey accessKey);

    class Updater{

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(User user){

            UserCache userCache = SpringContext.getBean(UserCache.class, CacheQualifiers.DEFAULT);
            UserData userData = new UserData(user.getUserId(),
                                             user.getAccessKey(),
                                             user.getSecretKey(),
                                             user.getRealmType(),
                                             user.getRealmId(),
                                             user.getPrincipalStatus());

            userCache.save(userData);
        }

        @PostRemove
        public void postRemove(User user) {

            UserCache userCache = SpringContext.getBean(UserCache.class, CacheQualifiers.DEFAULT);

            userCache.delete(user.getUserId());

        }



    }

}
