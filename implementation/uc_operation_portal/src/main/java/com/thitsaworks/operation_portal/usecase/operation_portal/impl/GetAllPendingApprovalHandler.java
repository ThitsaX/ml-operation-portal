package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllPendingApproval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetAllPendingApprovalHandler
    extends OperationPortalAuditableUseCase<GetAllPendingApproval.Input, GetAllPendingApproval.Output>
    implements GetAllPendingApproval {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllPendingApprovalHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ApprovalRequestQuery approvalRequestQuery;

    private final HubUserQuery hubUserQuery;

    public GetAllPendingApprovalHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ApprovalRequestQuery approvalRequestQuery,
                                        HubUserQuery hubUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.approvalRequestQuery = approvalRequestQuery;
        this.hubUserQuery = hubUserQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.approvalRequestQuery.getPendingApprovalRequests();

        return new Output(output.stream()
                                .map(request -> new Output.PendingApproval(request.approvalRequestId(),
                                                                           request.requestedAction(),
                                                                           request.dfsp(),
                                                                           request.currency(),
                                                                           request.amount(),
                                                                           this.getEmail(new HubUserId(request.requestedBy()
                                                                                                              .getId())),
                                                                           request.requestedDtm(),
                                                                           request.action()))
                                .toList());
    }

    private String getEmail(HubUserId hubUserId) {

        return this.hubUserQuery.find(hubUserId)
                                .map(user -> user.email()
                                                 .getValue())
                                .orElse("unknown");
    }

}
