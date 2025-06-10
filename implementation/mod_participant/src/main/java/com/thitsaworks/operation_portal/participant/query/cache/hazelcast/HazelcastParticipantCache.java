package com.thitsaworks.operation_portal.participant.query.cache.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.GetUserIds;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
@Qualifier(ParticipantCache.Strategies.HAZELCAST)
public class HazelcastParticipantCache implements ParticipantCache {

    private static final String HZ_PARTICIPANT = "hz_participant";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private GetUserIds getUserIds;

    @Override
    public void save(ParticipantData participantData) {

        GetUserIds.Output output = this.getUserIds.execute(new GetUserIds.Input(participantData.getParticipantId()));
        participantData.setParticipantUserIds(output.getParticipantUserIds());
        
        IMap<Long, ParticipantData> participantDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        participantDataIMap.put(participantData.getParticipantId().getId(), participantData);

    }

    @Override
    public ParticipantData get(ParticipantId participantId) {

        IMap<Long, ParticipantData> participantDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        return participantDataIMap.get(participantId.getId());
    }


    @Override
    public void delete(ParticipantId participantId) {

        IMap<Long, ParticipantData> principalDataIMap = this.hazelcastInstance.getMap(HZ_PARTICIPANT);
        principalDataIMap.delete(participantId.getId());
    }

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Participant participant) {

            ParticipantCache participantCache =
                    SpringContext.getBean(ParticipantCache.class, ParticipantCache.Strategies.HAZELCAST);
            ParticipantData participantData = new ParticipantData(participant);
            participantCache.save(participantData);
        }

        @PostRemove
        public void postRemove(Participant participant) {

            ParticipantCache principalCache =
                    SpringContext.getBean(ParticipantCache.class, ParticipantCache.Strategies.HAZELCAST);
            principalCache.delete(participant.getParticipantId());
        }

    }

}
