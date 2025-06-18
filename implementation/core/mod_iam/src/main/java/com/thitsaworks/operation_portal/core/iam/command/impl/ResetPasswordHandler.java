package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ResetPassword;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordHandler implements ResetPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordHandler.class);

    private final PrincipalRepository principalRepository;

    private final PrincipalCache principalCache;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws PrincipalNotFoundException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findOne(
                PrincipalRepository.Filters.withPrincipalId(input.principalId()));

        if (optionalPrincipal.isEmpty()) {

            throw new PrincipalNotFoundException();
        }

        Principal principal = optionalPrincipal.get();
        AccessKey oldAccessKey = principal.getAccessKey();

        principal.reset(input.password());

        principalRepository.save(principal);
        principalCache.delete(oldAccessKey);

        return new Output(principal.getAccessKey(), principal.getSecretKey(),true);
    }
}
