package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditByParticipantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAuditByParticipantListHandler
    extends OperationPortalAuditableUseCase<GetAuditByParticipantList.Input, GetAuditByParticipantList.Output>
    implements GetAuditByParticipantList {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditByParticipantListHandler.class);

    private final IAMQuery iamQuery;

    private final GetAllAuditByParticipantQuery getAllAuditByParticipantQuery;

    private final PrincipalRoleQuery principalRoleQuery;

    private final RoleQuery roleQuery;

    public GetAuditByParticipantListHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            IAMQuery iamQuery,
                                            GetAllAuditByParticipantQuery getAllAuditByParticipantQuery,
                                            PrincipalRoleQuery principalRoleQuery,
                                            RoleQuery roleQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.getAllAuditByParticipantQuery = getAllAuditByParticipantQuery;
        this.principalRoleQuery = principalRoleQuery;
        this.roleQuery = roleQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var grantedActionList = this.iamQuery.getGrantedActionsByPrincipal(new PrincipalId(input.auditedById()
                                                                                                .getEntityId()))
                                             .stream()
                                             .map(ActionData::actionId)
                                             .toList();

        var principalRole = this.principalRoleQuery.getRole(new PrincipalId(input.auditedById()
                                                                                 .getEntityId()));
        var role = this.roleQuery.get(principalRole.roleId());

        RealmId realmId = null;
        if (role.isDfsp()) {
            realmId = input.realmId();
        }

        GetAllAuditByParticipantQuery.Output output =
            this.getAllAuditByParticipantQuery.execute(new GetAllAuditByParticipantQuery.Input(
                realmId,
                input.fromDate(),
                input.toDate(),
                grantedActionList));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (var data : output.auditInfoList()) {

            auditInfoList.add(new Output.AuditInfo(data.date(),
                                                   data.action(),
                                                   data.madeBy()));
        }

        return new Output(auditInfoList);
    }

}
