package com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.ParticipantUser;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantUserData;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier(ParticipantUserCache.Strategies.HAZELCAST)
public class HazelcastParticipantUserCache implements ParticipantUserCache {

    private static final String HZ_PARTICIPANT_USER = "hz_participant_user";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(ParticipantUserData participantUserData) {

        IMap<Long, ParticipantUserData> participantUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        participantUserDataIMap.put(participantUserData.getParticipantUserId().getId(), participantUserData);
    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) {

        IMap<Long, ParticipantUserData> participantUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        return participantUserDataIMap.get(participantUserId.getId());
    }


    @Override
    public void delete(ParticipantUserId participantUserId) {

        IMap<Long, ParticipantUserData> principalUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        principalUserDataIMap.delete(participantUserId.getId());
    }

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(ParticipantUser participantUser) {

            ParticipantUserCache participantUserCache =
                    SpringContext.getBean(ParticipantUserCache.class, Strategies.HAZELCAST);
            ParticipantUserData participantUserData = new ParticipantUserData(participantUser);
            participantUserCache.save(participantUserData);
        }

        @PostRemove
        public void postRemove(ParticipantUser participant) {

            ParticipantUserCache principalUserCache =
                    SpringContext.getBean(ParticipantUserCache.class, Strategies.HAZELCAST);
            principalUserCache.delete(participant.getParticipantUserId());
        }

    }

}
