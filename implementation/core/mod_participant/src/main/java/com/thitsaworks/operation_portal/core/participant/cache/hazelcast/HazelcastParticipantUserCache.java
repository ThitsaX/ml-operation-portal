package com.thitsaworks.operation_portal.core.participant.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier(CacheQualifiers.HAZELCAST)
public class HazelcastParticipantUserCache implements ParticipantUserCache {

    private static final String HZ_PARTICIPANT_USER = "hz_participant_user";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(ParticipantUserData participantUserData) {

        IMap<Long, ParticipantUserData> participantUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        participantUserDataIMap.put(participantUserData.participantUserId()
                                                       .getId(), participantUserData);
    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) {

        IMap<Long, ParticipantUserData> participantUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        return participantUserDataIMap.get(participantUserId.getId());
    }

    @Override
    public void delete(ParticipantUserId participantUserId) {

        IMap<Long, ParticipantUserData> participantUserDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT_USER);
        participantUserDataIMap.delete(participantUserId.getId());

    }

}
