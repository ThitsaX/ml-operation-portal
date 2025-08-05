package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.query.GreetingQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetGreeting;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetGreetingHandler extends OperationPortalAuditableUseCase<GetGreeting.Input, GetGreeting.Output>
    implements GetGreeting {

    private static final Logger LOG = LoggerFactory.getLogger(GetGreetingHandler.class);

    private final GreetingQuery greetingQuery;

    public GetGreetingHandler(CreateInputAuditCommand createInputAuditCommand,
                              CreateOutputAuditCommand createOutputAuditCommand,
                              CreateExceptionAuditCommand createExceptionAuditCommand,
                              ObjectMapper objectMapper,
                              PrincipalCache principalCache,
                              ActionAuthorizationManager actionAuthorizationManager,
                              GreetingQuery greetingQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.greetingQuery = greetingQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var greeting = this.greetingQuery.get(input.greetingId());

        return new GetGreeting.Output(greeting.greetingId(),
                                      greeting.greetingTitle(),
                                      greeting.greetingDetail(),
                                      greeting.isDeleted());
    }

}
