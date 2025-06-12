package com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.proxy;

import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier(ParticipantCache.Strategies.PROXY)
public class ProxyParticipantCache implements ParticipantCache {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    @Qualifier(Strategies.HAZELCAST)
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

            participantData = new ParticipantData(optionalPrincipal.get());

            this.participantCache.save(participantData);
        }

        return participantData;
    }


    @Override
    public void delete(ParticipantId participantId) {

        this.participantCache.delete(participantId);

    }

}
