package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePrincipalCommandHandler implements CreatePrincipalCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePrincipalCommandHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findByPrincipalId(input.principalId());

        if (optionalPrincipal.isPresent()) {

            throw new IAMException(IAMErrors.DUPLICATE_PRINCIPAL.format(input.principalId().getId()));
        }

        Principal newPrincipal = new Principal(input.principalId(),
                                               input.passwordPlain(),
                                               input.realmId(),
                                               input.principalStatus());

        this.principalRepository.save(newPrincipal);

        return new Output(newPrincipal.getAccessKey(), newPrincipal.getSecretKey());
    }

}
