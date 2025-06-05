package com.thitsaworks.dfsp_portal.participant.query.cache.proxy;

import com.thitsaworks.dfsp_portal.participant.domain.ParticipantUser;
import com.thitsaworks.dfsp_portal.participant.domain.repository.ParticipantUserRepository;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.participant.query.cache.ParticipantUserCache;
import com.thitsaworks.dfsp_portal.participant.query.data.ParticipantUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier(ParticipantUserCache.Strategies.PROXY)
public class ProxyParticipantUserCache implements ParticipantUserCache {

    @Autowired
    private ParticipantUserRepository participantUserRepository;

    @Autowired
    @Qualifier(Strategies.HAZELCAST)
    private ParticipantUserCache participantUserCache;

    @Override
    public void save(ParticipantUserData participantUserData) {

        this.participantUserCache.save(participantUserData);
    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) {

        ParticipantUserData participantUserData = this.participantUserCache.get(participantUserId);

        if (participantUserData == null) {

            Optional<ParticipantUser> optionalPrincipal = this.participantUserRepository.findById(participantUserId);

            if (optionalPrincipal.isEmpty()) {

                return null;
            }

            participantUserData = new ParticipantUserData(optionalPrincipal.get());

            this.participantUserCache.save(participantUserData);
        }

        return participantUserData;
    }

    @Override
    public void delete(ParticipantUserId participantUserId) {

        this.participantUserCache.delete(participantUserId);

    }

}
