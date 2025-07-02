package com.thitsaworks.operation_portal.core.participant.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@Qualifier(CacheQualifiers.PROXY)
public class ProxyParticipantUserCache implements ParticipantUserCache {

    @Autowired
    private ParticipantUserRepository participantUserRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private ParticipantUserCache participantUserCache;

    @Override
    public void save(ParticipantUserData participantUserData) {

        this.participantUserCache.save(participantUserData);
    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) {

        ParticipantUserData participantUserData = this.participantUserCache.get(participantUserId);

        if (participantUserData == null) {

            Optional<ParticipantUser> optionalParticipantUser = this.participantUserRepository.findById(
                participantUserId);

            if (optionalParticipantUser.isEmpty()) {

                return null;
            }

            participantUserData = new ParticipantUserData(optionalParticipantUser.get());

            this.participantUserCache.save(participantUserData);
        }

        return participantUserData;
    }

    @Override
    public void delete(ParticipantUserId participantUserId) {

        this.participantUserCache.delete(participantUserId);

    }

}
