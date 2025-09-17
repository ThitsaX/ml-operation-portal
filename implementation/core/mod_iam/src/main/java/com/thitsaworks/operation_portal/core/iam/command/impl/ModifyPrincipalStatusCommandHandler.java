package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalStatusCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyPrincipalStatusCommandHandler implements ModifyPrincipalStatusCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyPrincipalStatusCommandHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public ModifyPrincipalStatusCommand.Output execute(Input input) throws IAMException {

        Principal principal = this.principalRepository.findByPrincipalId(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.format(
                                                          input.principalId()
                                                               .getId().toString())));

        this.principalRepository.save(principal.principalStatus(input.principalStatus()));

        return new ModifyPrincipalStatusCommand.Output(principal.getPrincipalId(), true);
    }

}
