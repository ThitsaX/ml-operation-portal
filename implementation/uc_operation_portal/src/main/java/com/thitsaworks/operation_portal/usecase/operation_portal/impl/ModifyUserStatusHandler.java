package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalStatusCommand;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyUserStatus;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyUserStatusHandler
    extends OperationPortalAuditableUseCase<ModifyUserStatus.Input, ModifyUserStatus.Output>
    implements ModifyUserStatus {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserStatusHandler.class);

    private final ModifyPrincipalStatusCommand modifyPrincipalStatusCommand;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    public ModifyUserStatusHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   ModifyPrincipalStatusCommand modifyPrincipalStatusCommand,
                                   UserPermissionManager userPermissionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyPrincipalStatusCommand = modifyPrincipalStatusCommand;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData requestingPrincipalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        PrincipalData principalData = this.principalCache.get(new PrincipalId(input.userId()
                                                                                   .getEntityId()));

        if (requestingPrincipalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.defaultMessage(
                    "Principal is not found for the user [" + input.userId().getId() + "]."));

        }

        var isDfsp = this.userPermissionManager.isDfsp(requestingPrincipalData.principalId());

        if (isDfsp) {

            if (!requestingPrincipalData.realmId()
                                        .equals(principalData.realmId())) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_USER_ACCESS);
            }

        }

        this.modifyPrincipalStatusCommand.execute(
            new ModifyPrincipalStatusCommand.Input(new PrincipalId(input.userId()
                                                                        .getId()),
                                                   input.activeStatus()));

        return new Output(true, input.userId());

    }

}
