package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetPendingApprovalList;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetPendingApprovalListHandler
    extends OperationPortalAuditableUseCase<GetPendingApprovalList.Input, GetPendingApprovalList.Output>
    implements GetPendingApprovalList {

    private static final Logger LOG = LoggerFactory.getLogger(GetPendingApprovalListHandler.class);

    private final ApprovalRequestQuery approvalRequestQuery;

    private final Utility utility;

    public GetPendingApprovalListHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         ApprovalRequestQuery approvalRequestQuery,
                                         Utility utility
                                        ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.approvalRequestQuery = approvalRequestQuery;
        this.utility = utility;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.approvalRequestQuery.getPendingApprovalRequests();

        return new Output(output.stream()
                                .map(request -> new Output.PendingApproval(request.approvalRequestId(),
                                                                           request.requestedAction(),
                                                                           request.participantId(),
                                                                           request.currency(),
                                                                           request.amount(),
                                                                           this.utility.getEmail(new UserId(
                                                                                   request.requestedBy()
                                                                                          .getId())),
                                                                           request.requestedDtm(),
                                                                           request.action()))
                                .toList());
    }

}
