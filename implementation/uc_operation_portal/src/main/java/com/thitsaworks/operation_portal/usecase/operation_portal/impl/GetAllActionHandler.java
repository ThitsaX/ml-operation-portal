package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.data.ActionData;
import com.thitsaworks.operation_portal.core.audit.query.ActionQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;

import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllAction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetAllActionHandler extends OperationPortalAuditableUseCase<GetAllAction.Input, GetAllAction.Output>
    implements GetAllAction {

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final ActionQuery actionQuery;

    public GetAllActionHandler(CreateInputAuditCommand createInputAuditCommand,
                               CreateOutputAuditCommand createOutputAuditCommand,
                               CreateExceptionAuditCommand createExceptionAuditCommand,
                               ObjectMapper objectMapper,
                               PrincipalCache principalCache, ActionQuery actionQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.actionQuery = actionQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.actionQuery
                         .getAction()
                         .stream()
                         .map(action -> new Output.ActionName(action.actionId(), action.name()))
                         .collect(Collectors.toList());

        return new Output(output);
    }
}