package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ResetPasswordCommand;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ResetCurrentPassword;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResetCurrentPasswordHandler
    extends OperationPortalAuditableUseCase<ResetCurrentPassword.Input, ResetCurrentPassword.Output>
    implements ResetCurrentPassword {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentPasswordHandler.class);

    private final ResetPasswordCommand resetPasswordCommand;

    private final UserQuery userQuery;

    public ResetCurrentPasswordHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       ResetPasswordCommand resetPasswordCommand,
                                       UserQuery userQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.resetPasswordCommand = resetPasswordCommand;
        this.userQuery = userQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        UserData userData =
            this.userQuery.get(input.email());

        if (userData.userId() == null) {

            throw new ParticipantException(ParticipantErrors.EMAIL_NOT_FOUND);
        }

        ResetPasswordCommand.Output resetPasswordOutput = this.resetPasswordCommand.execute(
            new ResetPasswordCommand.Input(new PrincipalId(userData.userId()
                                                                   .getId()),
                                           input.password()));

        return new Output(resetPasswordOutput.accessKey(),
                          resetPasswordOutput.secretKey(),
                          resetPasswordOutput.updated());
    }

}
