package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyPrincipalHandler implements ModifyPrincipal {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyPrincipalHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public ModifyPrincipal.Output execute(Input input) throws PrincipalNotFoundException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findById(input.getPrincipalId());

        if (optionalPrincipal.isEmpty()) {

            throw new PrincipalNotFoundException();

        }

        Principal principal = optionalPrincipal.get();

        this.principalRepository.save(principal.modify(input.getPrincipalStatus(), input.getUserRoleType()));

        return new ModifyPrincipal.Output(principal.getPrincipalId(), true);
    }

}
