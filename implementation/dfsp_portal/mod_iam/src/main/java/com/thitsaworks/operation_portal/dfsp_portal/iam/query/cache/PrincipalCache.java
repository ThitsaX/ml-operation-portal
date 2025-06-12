package com.thitsaworks.operation_portal.dfsp_portal.iam.query.cache;

import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.RealmType;

public interface PrincipalCache {

    void save(PrincipalData principalData);

    PrincipalData get(AccessKey accessKey);

    PrincipalData get(PrincipalId principalId);

    PrincipalData get(AccessKey accessKey, RealmType realmType);

    void delete(AccessKey accessKey);

    class Strategies {

        public static final String HAZELCAST = "Hazelcast";

        public static final String PROXY = "Proxy";

        public static final String DEFAULT = PROXY;

    }

}
