package com.thitsaworks.dfsp_portal.participant.query.cache;

import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.query.data.ParticipantData;
import com.thitsaworks.dfsp_portal.participant.type.DfspCode;

public interface ParticipantCache {

    void save(ParticipantData participantData);

    ParticipantData get(ParticipantId participantId);

    void delete(ParticipantId participantId);

    class Strategies {

        public static final String HAZELCAST = "Hazelcast";

        public static final String PROXY = "Proxy";

        public static final String DEFAULT = PROXY;

    }

}
