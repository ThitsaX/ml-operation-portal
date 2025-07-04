package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.SystemException;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.Authenticate;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.LoginUserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class LoginUserAccountHandler extends CommonAuditableUseCase<LoginUserAccount.Input, LoginUserAccount.Output>
    implements LoginUserAccount {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ParticipantUserQuery participantUserQuery;

    private final Authenticate authenticate;

    private final ObjectMapper objectMapper;

    public LoginUserAccountHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ParticipantUserQuery participantUserQuery,
                                   Authenticate authenticate) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.participantUserQuery = participantUserQuery;
        this.authenticate = authenticate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws ParticipantException, IAMException, IAMIgnorableException {

        ParticipantUserData participantUserData = this.participantUserQuery.get(input.email());

        if (participantUserData.participantUserId() == null) {

            throw new ParticipantException(ParticipantErrors.EMAIL_NOT_FOUND);
        }

        Authenticate.Output securityToken = this.authenticate.execute(
            new Authenticate.Input(new PrincipalId(participantUserData.participantUserId()
                                                                      .getId()),
                                   input.passwordPlain()));

        return new Output(securityToken.securityToken()
                                       .getAccessKey(),
                          securityToken.securityToken()
                                       .getSecretKey());
    }

    @Override
    protected void beforeExecute(Input input)  {

        /*
         * Audit Logging:
         * Logs input parameters and output results for auditing and traceability.
         */
        String inputJson, inputInfo;
        try {

            inputJson = this.objectMapper.writeValueAsString(input);
            inputInfo = MaskPassword.maskPassword(this.objectMapper, inputJson);

        } catch (Exception e) {
            throw new SystemException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }

        // var principalId = principalData.principalId();

//        CommonAuditableUseCase.auditId.set(this.createInputAuditCommand.execute(new CreateInputAuditCommand.Input(this.getName(),
//                                                                                                                  new UserId(1L),
//                                                                                                                   new RealmId(1L),
//                                                                                                                  inputInfo))
//                                                                       .auditId());
    }

    @Override
    protected void afterExecute(Output output) {

    }

}
