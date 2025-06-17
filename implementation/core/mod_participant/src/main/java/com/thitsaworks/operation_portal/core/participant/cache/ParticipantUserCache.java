package com.thitsaworks.operation_portal.core.participant.cache;

import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface ParticipantUserCache {

    void save(ParticipantUserData participantUserData);

    ParticipantUserData get(ParticipantUserId participantUserId);

    void delete(ParticipantUserId participantUserId);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(ParticipantUser participantUser) {

            ParticipantUserCache participantCache = SpringContext.getBean(ParticipantUserCache.class,
                                                                          CacheQualifiers.DEFAULT);

            ParticipantUserData participantUserData = new ParticipantUserData(participantUser);

            participantCache.save(participantUserData);
        }

        @PostRemove
        public void postRemove(ParticipantUser participantUser) {

            ParticipantUserCache participantUserCache = SpringContext.getBean(ParticipantUserCache.class,
                                                                              CacheQualifiers.DEFAULT);
            participantUserCache.delete(participantUser.getParticipantUserId());

        }

    }

}
