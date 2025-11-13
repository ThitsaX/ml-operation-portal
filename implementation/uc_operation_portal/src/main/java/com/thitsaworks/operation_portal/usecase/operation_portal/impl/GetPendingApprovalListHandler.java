package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.ActionQuery;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetPendingApprovalList;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPendingApprovalListHandler
    extends OperationPortalUseCase<GetPendingApprovalList.Input, GetPendingApprovalList.Output>
    implements GetPendingApprovalList {

    private static final Logger LOG = LoggerFactory.getLogger(GetPendingApprovalListHandler.class);

    private final ApprovalRequestQuery approvalRequestQuery;

    private final Utility utility;

    private final UserPermissionManager userPermissionManager;

    private final IAMQuery iamQuery;

    private final ActionQuery actionQuery;

    public GetPendingApprovalListHandler(PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         ApprovalRequestQuery approvalRequestQuery,
                                         Utility utility,
                                         UserPermissionManager userPermissionManager,
                                         ActionQuery actionQuery,
                                         IAMQuery iamQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.approvalRequestQuery = approvalRequestQuery;
        this.utility = utility;
        this.userPermissionManager = userPermissionManager;
        this.actionQuery = actionQuery;
        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();
        var principalId = currentUser.principalId();
        final List<ApprovalRequestData> requests;

        if (hasApprovalPermissions(principalId)) {

            requests = this.approvalRequestQuery.getPendingApprovalRequests();
        } else {

            requests =
                this.approvalRequestQuery.getPendingApprovalRequestsByRequestedId(new UserId(principalId.getId()));
        }

        return new Output(requests.stream()
                                  .map(request -> new Output.PendingApproval(
                                      request.getApprovalRequestId(),
                                      request.getFundInOutAction(),
                                      request.getParticipantName(),
                                      request.getCurrency(),
                                      request.getAmount(),
                                      this.utility.getEmail(new UserId(request.getRequestedBy()
                                                                              .getId())),
                                      request.getRequestedDtm(),
                                      request.getAction()
                                  ))
                                  .toList()
        );
    }

    private boolean hasApprovalPermissions(PrincipalId principalId) throws IAMException {

        final var grantedActions = this.iamQuery.getGrantedActionsByPrincipal(principalId);

        final var createApprovalRequest = this.actionQuery.get(new ActionCode("CreateApprovalRequest"));
        final var modifyApprovalAction = this.actionQuery.get(new ActionCode("ModifyApprovalAction"));

        return grantedActions.contains(createApprovalRequest)
                   && grantedActions.contains(modifyApprovalAction);

    }

}
