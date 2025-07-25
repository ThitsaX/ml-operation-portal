package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface UserCache {

    void  save (UserData userData);

    UserData get(AccessKey accessKey);
    UserData get(UserId userId);
    UserData get(AccessKey accessKey, RealmType realmType);

    void delete(AccessKey accessKey);


    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(User user) {

            UserCache userCache = SpringContext.getBean(
                UserCache.class,
                CacheQualifiers.DEFAULT);

            UserData userData = new UserData(user);

            userCache.save(userData);
        }

        @PostRemove
        public void postRemove(User user) {

            UserCache userCache = SpringContext.getBean(
                UserCache.class,
                CacheQualifiers.DEFAULT);
            userCache.delete(user.getAccessKey());

        }

    }


}
