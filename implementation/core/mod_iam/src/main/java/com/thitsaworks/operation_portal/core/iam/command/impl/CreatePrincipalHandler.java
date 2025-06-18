package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.core.iam.exception.DuplicatePrincipalException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePrincipalHandler implements CreatePrincipal {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws DuplicatePrincipalException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findOne(
                PrincipalRepository.Filters.withPrincipalId(input.principalId())
                                           .and(PrincipalRepository.Filters.withRealm(input.realmType())));

        if (optionalPrincipal.isPresent()) {

            throw new DuplicatePrincipalException(input.principalId().getId().toString());
        }

        Principal newPrincipal = new Principal(input.principalId(), input.realmType(), input.passwordPlain(),
                                               input.realmId(), input.userRoleType(), input.principalStatus());

        this.principalRepository.save(newPrincipal);

        return new Output(newPrincipal.getAccessKey(), newPrincipal.getSecretKey());
    }

}
