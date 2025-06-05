package com.thitsaworks.dfsp_portal.participant.query.cache;

import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.participant.query.data.ParticipantUserData;
import com.thitsaworks.dfsp_portal.participant.type.DfspCode;

public interface ParticipantUserCache {

    void save(ParticipantUserData participantUserData);

    ParticipantUserData get(ParticipantUserId participantUserId);


    void delete(ParticipantUserId participantUserId);

    class Strategies {

        public static final String HAZELCAST = "Hazelcast";

        public static final String PROXY = "Proxy";

        public static final String DEFAULT = PROXY;

    }

}
