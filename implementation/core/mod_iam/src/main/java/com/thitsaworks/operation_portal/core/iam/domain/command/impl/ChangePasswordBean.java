package com.thitsaworks.operation_portal.core.iam.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import com.thitsaworks.operation_portal.core.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.domain.command.ChangePassword;
import com.thitsaworks.component.common.identifier.AccessKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChangePasswordBean implements ChangePassword {

    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordBean.class);

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    @Qualifier(CacheQualifiers.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws PasswordAuthenticationFailureException, PrincipalNotFoundException {

        Optional<Principal> optionalPrincipal =
                this.principalRepository.findOne(PrincipalRepository.Filters.withPrincipalId(input.getPrincipalId()));

        if (optionalPrincipal.isEmpty()) {

            throw new PrincipalNotFoundException();
        }

        Principal principal = optionalPrincipal.get();
        AccessKey oldAccessKey = principal.getAccessKey();

        principal.change(input.getNewPassword(), input.getOldPassword());

        principalRepository.save(principal);

        principalCache.delete(oldAccessKey);

        return new Output(principal.getAccessKey(), principal.getSecretKey());
    }

}
