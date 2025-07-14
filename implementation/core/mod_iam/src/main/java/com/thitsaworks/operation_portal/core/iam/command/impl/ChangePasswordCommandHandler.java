package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ChangePasswordCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordCommandHandler implements ChangePasswordCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordCommandHandler.class);

    private final PrincipalRepository principalRepository;

    private final PrincipalCache principalCache;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException, IAMIgnorableException {

        Principal principal = this.principalRepository.findByPrincipalId(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        AccessKey oldAccessKey = principal.getAccessKey();

        principal.change(input.newPassword(), input.oldPassword());

        principalRepository.save(principal);

        principalCache.delete(oldAccessKey);

        return new Output(principal.getAccessKey(), principal.getSecretKey());
    }

}
