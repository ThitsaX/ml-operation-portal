package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;

import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditByParticipantList;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAuditByParticipantListHandler
    extends OperationPortalUseCase<GetAuditByParticipantList.Input, GetAuditByParticipantList.Output>
    implements GetAuditByParticipantList {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditByParticipantListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final GetAllAuditByParticipantQuery getAllAuditByParticipantQuery;


    public GetAuditByParticipantListHandler(PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            GetAllAuditByParticipantQuery getAllAuditByParticipantQuery) {

        super(PERMITTED_ROLES,
              principalCache,
              actionAuthorizationManager);

        this.getAllAuditByParticipantQuery = getAllAuditByParticipantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetAllAuditByParticipantQuery.Output output =
            this.getAllAuditByParticipantQuery.execute(new GetAllAuditByParticipantQuery.Input(
                input.realmId(),
                input.fromDate(),
                input.toDate(),
                input.userId(),
                input.actionName()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAllAuditByParticipantQuery.Output.AuditInfo data : output.getAuditInfoList()) {

            auditInfoList.add(new Output.AuditInfo(
                data.getUserName(),
                data.getActionName(),
                data.getActionDate()));
        }

        return new Output(auditInfoList);
    }

}
