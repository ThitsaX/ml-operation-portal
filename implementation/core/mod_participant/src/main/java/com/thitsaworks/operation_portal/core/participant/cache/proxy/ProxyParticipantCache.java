package com.thitsaworks.operation_portal.core.participant.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Primary
@Component
@Qualifier(CacheQualifiers.PROXY)
public class ProxyParticipantCache implements ParticipantCache {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    @Qualifier(CacheQualifiers.HAZELCAST)
    private ParticipantCache participantCache;

    @Override
    public void save(ParticipantData participantData) {

        this.participantCache.save(participantData);
    }

    @Override
    public ParticipantData get(ParticipantId participantId) {

        ParticipantData participantData = this.participantCache.get(participantId);

        if (participantData == null) {

            Optional<Participant> optionalPrincipal = this.participantRepository.findById(participantId);

            if (optionalPrincipal.isEmpty()) {

                return null;
            }

            Participant participant = optionalPrincipal.get();

            participantData = new ParticipantData(participant);

            this.participantCache.save(participantData);
        }

        return participantData;
    }


    @Override
    public void delete(ParticipantId participantId) {

        this.participantCache.delete(participantId);

    }

}
