package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoLogging
public class GetAuditListByParticipantHandler
    extends OperationPortalUseCase<GetAuditListByParticipant.Input, GetAuditListByParticipant.Output>
    implements GetAuditListByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListByParticipantHandler.class);

    private final IAMQuery iamQuery;

    private final GetAllAuditByParticipantQuery getAllAuditByParticipantQuery;

    private final UserPermissionManager userPermissionManager;

    public GetAuditListByParticipantHandler(PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            IAMQuery iamQuery,
                                            GetAllAuditByParticipantQuery getAllAuditByParticipantQuery,
                                            UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.getAllAuditByParticipantQuery = getAllAuditByParticipantQuery;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        var existingRealmId = currentUser.realmId();

        var grantedActionList = this.iamQuery.getGrantedActionListByPrincipal(currentUser.principalId())
                                             .stream()
                                             .map(ActionData::actionId)
                                             .toList();

        RealmId realmId = null;
        if (this.userPermissionManager.isDfsp(currentUser.principalId())) {
            realmId = existingRealmId;
        }

        GetAllAuditByParticipantQuery.Output output =
            this.getAllAuditByParticipantQuery.execute(new GetAllAuditByParticipantQuery.Input(
                realmId,
                input.fromDate(),
                input.toDate(),
                grantedActionList,
                input.userId(),
                input.actionid(),
                input.page(),
                input.pageSize()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (var data : output.auditInfoList()) {

            auditInfoList.add(new Output.AuditInfo(data.auditId(),
                                                   data.date(),
                                                   data.action(),
                                                   data.madeBy(),
                                                   data.traceId()));
        }

        return new Output(auditInfoList, output.totalElements(), output.totalPages());
    }

}
