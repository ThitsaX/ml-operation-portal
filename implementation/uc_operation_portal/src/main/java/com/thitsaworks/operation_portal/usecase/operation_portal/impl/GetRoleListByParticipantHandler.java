package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRoleListByParticipantHandler
    extends OperationPortalAuditableUseCase<GetRoleListByParticipant.Input, GetRoleListByParticipant.Output>
    implements GetRoleListByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetRoleListByParticipantHandler.class);

    private final RoleQuery roleQuery;

    private final UserPermissionManager userPermissionManager;

    public GetRoleListByParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           RoleQuery roleQuery,
                                           UserPermissionManager userPermissionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.roleQuery = roleQuery;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<RoleData> roleList = this.roleQuery.getAll();

        boolean isDfspUser = this.userPermissionManager.isDfsp(new PrincipalId(input.userId()
                                                                                    .getId()));

        if (isDfspUser) {
            roleList = roleList.stream()
                               .filter(role -> role.name() != null && role.name()
                                                                          .startsWith("DFSP"))
                               .toList();
        }

        return new Output(roleList);
    }

}
