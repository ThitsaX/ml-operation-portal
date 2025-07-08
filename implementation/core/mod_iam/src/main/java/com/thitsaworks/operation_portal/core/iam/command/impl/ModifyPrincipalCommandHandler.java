package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalCommand;
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
public class ModifyPrincipalCommandHandler implements ModifyPrincipalCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyPrincipalCommandHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public ModifyPrincipalCommand.Output execute(Input input) throws IAMException {

        Principal principal = this.principalRepository.findByPrincipalId(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        this.principalRepository.save(principal.modify(input.principalStatus(), input.userRoleType()));

        return new ModifyPrincipalCommand.Output(principal.getPrincipalId(), true);
    }

}
