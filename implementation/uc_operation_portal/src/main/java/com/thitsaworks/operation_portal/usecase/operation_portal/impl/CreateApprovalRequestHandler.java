package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.approval.command.CreateApprovalRequestCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateApprovalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class CreateApprovalRequestHandler
    extends OperationPortalAuditableUseCase<CreateApprovalRequest.Input, CreateApprovalRequest.Output>
    implements CreateApprovalRequest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateApprovalRequestHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final CreateApprovalRequestCommand createApprovalRequestCommand;

    public CreateApprovalRequestHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        CreateApprovalRequestCommand createApprovalRequestCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createApprovalRequestCommand = createApprovalRequestCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.createApprovalRequestCommand.execute(new CreateApprovalRequestCommand.Input(input.requestedAction(),
                                                                                             input.dfsp(),
                                                                                             input.currency(),
                                                                                             input.amount(),
                                                                                             input.requestedBy()));

        return new Output(output.approvalRequestId());
    }

}
