package com.thitsaworks.dfsp_portal.iam.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.dfsp_portal.iam.domain.command.CreatePrincipal;
import com.thitsaworks.dfsp_portal.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.dfsp_portal.iam.exception.DuplicatePrincipalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreatePrincipalBean implements CreatePrincipal {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalBean.class);

    @Autowired
    private PrincipalRepository principalRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws DuplicatePrincipalException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findOne(
                PrincipalRepository.Filters.withPrincipalId(input.getPrincipalId())
                                           .and(PrincipalRepository.Filters.withRealm(input.getRealmType())));

        if (optionalPrincipal.isPresent()) {

            throw new DuplicatePrincipalException(input.getPrincipalId().getId().toString());
        }

        Principal newPrincipal = new Principal(input.getPrincipalId(), input.getRealmType(), input.getPasswordPlain(),
                input.getRealmId(),input.getUserRoleType(),input.getActiveStatus());

        this.principalRepository.save(newPrincipal);

        return new Output(newPrincipal.getAccessKey(), newPrincipal.getSecretKey());
    }

}
