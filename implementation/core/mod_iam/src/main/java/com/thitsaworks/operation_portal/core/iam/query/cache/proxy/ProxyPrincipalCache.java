package com.thitsaworks.operation_portal.core.iam.query.cache.proxy;

import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import com.thitsaworks.operation_portal.core.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.data.PrincipalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
@Qualifier(CacheQualifiers.PROXY)
public class ProxyPrincipalCache implements PrincipalCache {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    @Qualifier(CacheQualifiers.HAZELCAST)
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
