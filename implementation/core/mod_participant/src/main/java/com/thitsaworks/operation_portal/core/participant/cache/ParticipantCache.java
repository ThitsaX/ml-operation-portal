package com.thitsaworks.operation_portal.core.participant.cache;

import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public interface ParticipantCache {

    void save(ParticipantData participantData);

    ParticipantData get(ParticipantId participantId);

    void delete(ParticipantId participantId);

    public static class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Participant participant) {

            ParticipantCache participantCache = SpringContext.getBean(ParticipantCache.class, CacheQualifiers.DEFAULT);

            ParticipantData participantData = new ParticipantData(participant);

            participantCache.save(participantData);
        }

        @PostRemove
        public void postRemove(Participant participant) {

            ParticipantCache participantCache = SpringContext.getBean(ParticipantCache.class, CacheQualifiers.DEFAULT);
            participantCache.delete(participant.getParticipantId());

        }

    }

}
