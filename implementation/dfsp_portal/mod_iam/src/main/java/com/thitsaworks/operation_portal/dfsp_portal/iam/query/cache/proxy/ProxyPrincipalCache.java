package com.thitsaworks.operation_portal.dfsp_portal.iam.query.cache.proxy;

import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.RealmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier(PrincipalCache.Strategies.PROXY)
public class ProxyPrincipalCache implements PrincipalCache {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    @Qualifier(Strategies.HAZELCAST)
    private PrincipalCache principalCache;

    @Override
    public void save(PrincipalData principalData) {

        this.principalCache.save(principalData);
    }

    @Override
    public PrincipalData get(AccessKey accessKey) {

        PrincipalData principalData = this.principalCache.get(accessKey);

        if (principalData == null) {

            Optional<Principal> optionalPrincipal =
                    this.principalRepository.findOne(PrincipalRepository.Filters.withAccessKey(accessKey));

            if (optionalPrincipal.isEmpty()) {

                return null;
            }

            principalData = new PrincipalData(optionalPrincipal.get());

            this.principalCache.save(principalData);
        }

        return principalData;
    }

    @Override
    public PrincipalData get(PrincipalId principalId) {

        PrincipalData principalData = this.principalCache.get(principalId);

        if (principalData == null) {

            Optional<Principal> optionalPrincipal =
                    this.principalRepository.findOne(PrincipalRepository.Filters.withPrincipalId(principalId));

            if (optionalPrincipal.isEmpty()) {

                return null;
            }

            principalData = new PrincipalData(optionalPrincipal.get());

            this.principalCache.save(principalData);
        }

        return principalData;
    }

    @Override
    public PrincipalData get(AccessKey accessKey, RealmType realmType) {

        PrincipalData principalData = this.principalCache.get(accessKey, realmType);

        if (principalData == null) {

            Optional<Principal> optionalPrincipal = this.principalRepository.findOne(
                    PrincipalRepository.Filters.withAccessKey(accessKey)
                                               .and(PrincipalRepository.Filters.withRealm(realmType)));

            if (optionalPrincipal.isEmpty()) {

                return null;
            }

            principalData = new PrincipalData(optionalPrincipal.get());

            this.principalCache.save(principalData);
        }

        return principalData;
    }

    @Override
    public void delete(AccessKey accessKey) {

    }

}
