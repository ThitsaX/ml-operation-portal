package com.thitsaworks.operation_portal.core.participant.cache;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.model.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface UserCache {

    void save(UserData userData);

    UserData get(UserId userId);

    void delete(UserId userId);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(User user) {

            UserCache participantCache = SpringContext.getBean(UserCache.class,
                                                               CacheQualifiers.DEFAULT);

            UserData userData = new UserData(user);

            participantCache.save(userData);
        }

        @PostRemove
        public void postRemove(User user) {

            UserCache userCache = SpringContext.getBean(UserCache.class,
                                                        CacheQualifiers.DEFAULT);
            userCache.delete(user.getUserId());

        }

    }

}
