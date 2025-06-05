package com.thitsaworks.dfsp_portal.iam.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.dfsp_portal.iam.domain.command.ChangePassword;
import com.thitsaworks.dfsp_portal.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
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
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
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
