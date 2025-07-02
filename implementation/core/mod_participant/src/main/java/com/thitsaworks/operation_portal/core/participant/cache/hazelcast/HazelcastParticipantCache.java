package com.thitsaworks.operation_portal.core.participant.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier(CacheQualifiers.HAZELCAST)
public class HazelcastParticipantCache implements ParticipantCache {

    private static final String HZ_PARTICIPANT = "hz_participant";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Override
    public void save(ParticipantData participantData) {

        IMap<Long, ParticipantData> participantDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        participantDataIMap.put(participantData.participantId().getId(), participantData);

    }

    @Override
    public ParticipantData get(ParticipantId participantId) {

        IMap<Long, ParticipantData> participantDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        return participantDataIMap.get(participantId.getId());
    }


    @Override
    public void delete(ParticipantId participantId) {

        IMap<Long, ParticipantData> participantDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        participantDataIMap.delete(participantId.getId());
    }

}
