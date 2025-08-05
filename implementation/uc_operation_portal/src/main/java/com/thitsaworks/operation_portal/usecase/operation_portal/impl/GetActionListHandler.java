package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GetActionListHandler extends OperationPortalAuditableUseCase<GetActionList.Input, GetActionList.Output>
    implements GetActionList {

    private final IAMQuery iamQuery;

    public GetActionListHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                IAMQuery iamQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.iamQuery
                         .getActions()
                         .stream()
                         .map(action -> new Output.ActionName(action.actionId(), action.actionCode()
                                                                                       .getValue()))
                         .collect(Collectors.toList());

        return new Output(output);
    }

}