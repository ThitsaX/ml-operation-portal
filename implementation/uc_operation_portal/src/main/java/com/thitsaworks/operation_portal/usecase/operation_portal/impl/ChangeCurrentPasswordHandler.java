package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ChangePasswordCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ChangeCurrentPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChangeCurrentPasswordHandler
    extends OperationPortalAuditableUseCase<ChangeCurrentPassword.Input, ChangeCurrentPassword.Output>
    implements ChangeCurrentPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ChangePasswordCommand changePasswordCommand;

    @Autowired
    public ChangeCurrentPasswordHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        ChangePasswordCommand changePasswordCommand,
                                        PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.changePasswordCommand = changePasswordCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ChangePasswordCommand.Output changePasswordOutput = this.changePasswordCommand.execute(
            new ChangePasswordCommand.Input(input.principalId(), input.oldPassword(),
                                            input.newPassword()));

        return new Output(changePasswordOutput.accessKey(),
                          changePasswordOutput.secretKey());
    }

}
