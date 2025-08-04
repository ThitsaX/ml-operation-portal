package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.RemoveParticipantUserCommand;

import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveExistingParticipantUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemoveExistingParticipantUserHandler
    extends OperationPortalAuditableUseCase<RemoveExistingParticipantUser.Input, RemoveExistingParticipantUser.Output>
    implements RemoveExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final RemoveParticipantUserCommand removeParticipantUserCommand;

    private final ModifyPrincipalCommand modifyPrincipalCommand;

    private final PrincipalCache principalCache;

    public RemoveExistingParticipantUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                                CreateOutputAuditCommand createOutputAuditCommand,
                                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                                ObjectMapper objectMapper,
                                                PrincipalCache principalCache,
                                                ActionAuthorizationManager actionAuthorizationManager,
                                                RemoveParticipantUserCommand removeParticipantUserCommand,
                                                ModifyPrincipalCommand modifyPrincipalCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.removeParticipantUserCommand = removeParticipantUserCommand;
        this.modifyPrincipalCommand = modifyPrincipalCommand;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {

            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId()
                                  .getId()
                                  .equals(input.participantId()
                                               .getId())) {

                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }
        }

        RemoveParticipantUserCommand.Output output = this.removeParticipantUserCommand.execute(
            new RemoveParticipantUserCommand.Input(input.participantId(), input.participantUserId()));

        this.modifyPrincipalCommand.execute(
            new ModifyPrincipalCommand.Input(new PrincipalId(output.participantUserId()
                                                                   .getId()),
                                             PrincipalStatus.INACTIVE));

        return new RemoveExistingParticipantUser.Output(output.removed(), output.participantUserId());

    }

}
