package com.thitsaworks.operation_portal.core.iam.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import com.thitsaworks.operation_portal.core.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.exception.DuplicatePrincipalException;
import com.thitsaworks.operation_portal.core.iam.domain.command.CreatePrincipal;
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
    @DfspWriteTransactional
    public Output execute(Input input) throws DuplicatePrincipalException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findOne(
                PrincipalRepository.Filters.withPrincipalId(input.getPrincipalId())
                                           .and(PrincipalRepository.Filters.withRealm(input.getRealmType())));

        if (optionalPrincipal.isPresent()) {

            throw new DuplicatePrincipalException(input.getPrincipalId().getId().toString());
        }

        Principal newPrincipal = new Principal(input.getPrincipalId(), input.getRealmType(), input.getPasswordPlain(),
                                               input.getRealmId(), input.getUserRoleType());

        this.principalRepository.save(newPrincipal);

        return new Output(newPrincipal.getAccessKey(), newPrincipal.getSecretKey());
    }

}
