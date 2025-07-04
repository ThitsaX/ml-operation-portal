package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ChangePassword;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.ChangeCurrentPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChangeCurrentPasswordHandler
    extends CommonAuditableUseCase<ChangeCurrentPassword.Input, ChangeCurrentPassword.Output>
    implements ChangeCurrentPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ChangePassword changePassword;

    @Autowired
    public ChangeCurrentPasswordHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        ChangePassword changePassword,
                                        PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.changePassword = changePassword;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ChangePassword.Output changePasswordOutput = this.changePassword.execute(
            new ChangePassword.Input(input.principalId(), input.oldPassword(),
                                     input.newPassword()));

        return new Output(changePasswordOutput.accessKey(),
                          changePasswordOutput.secretKey());
    }

}
