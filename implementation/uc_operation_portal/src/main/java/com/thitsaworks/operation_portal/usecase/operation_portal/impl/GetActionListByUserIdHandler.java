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
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByUserId;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantUserListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetActionListByUserIdHandler extends OperationPortalAuditableUseCase<GetActionListByUserId.Input, GetActionListByUserId.Output>
    implements GetActionListByUserId {

    private final IAMQuery iamQuery;

    private final PrincipalCache principalCache;

    private final PrincipalRoleQuery principalRoleQuery;

    private final RoleQuery roleQuery;

    public GetActionListByUserIdHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        IAMQuery iamQuery, PrincipalCache principalCache1,
                                        PrincipalRoleQuery principalRoleQuery,
                                        RoleQuery roleQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.principalCache = principalCache1;
        this.principalRoleQuery = principalRoleQuery;
        this.roleQuery = roleQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        }

        Set<GetParticipantUserListByParticipant.Output.User> madeByUsers = new HashSet<>();
        var principalRole = this.principalRoleQuery.getRole(principalData.principalId());

        var role = this.roleQuery.get(principalRole.roleId());

        if (role.isDfsp()) {

            var output = this.iamQuery.getGrantedActionsByPrincipal(new PrincipalId(principalData.principalId()
                                                                                                 .getId()))
                                                 .stream()
                                                 .map(action -> new Output.ActionName(action.actionId(),
                                                                                      action.actionCode()
                                                                                            .getValue()))
                                                 .toList();

            return new Output(output);

        } else {

            var output = this.iamQuery
                             .getActions()
                             .stream()
                             .map(action -> new Output.ActionName(action.actionId(), action.actionCode()
                                                                                           .getValue()))
                             .collect(Collectors.toList());

            return new Output(output);
        }

    }
}