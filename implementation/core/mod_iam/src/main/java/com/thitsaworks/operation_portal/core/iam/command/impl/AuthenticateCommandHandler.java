package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.AuthenticateCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.SecurityToken;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateCommandHandler implements AuthenticateCommand {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateCommandHandler.class);

    private final PrincipalRepository principalRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException, IAMIgnorableException {

        Principal principal = this.principalRepository.findByPrincipalId(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        if (principal.getPrincipalStatus()
                     .equals(PrincipalStatus.INACTIVE)) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);
        }
        SecurityToken securityToken = principal.authenticate(input.passwordPlain());

        this.principalRepository.save(principal);

        return new AuthenticateCommand.Output(securityToken);
    }

}
