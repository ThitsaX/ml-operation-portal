package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.home_message.query.GreetingQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetGreeting;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetGreetingHandler extends HubOperatorAuditableUseCase<GetGreeting.Input, GetGreeting.Output> implements GetGreeting {

    private  final GreetingQuery greetingQuery;

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    public GetGreetingHandler(CreateInputAuditCommand createInputAuditCommand,
                              CreateOutputAuditCommand createOutputAuditCommand,
                              CreateExceptionAuditCommand createExceptionAuditCommand,
                              ObjectMapper objectMapper,
                              PrincipalCache principalCache, GreetingQuery greetingQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.greetingQuery = greetingQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var greeting = this.greetingQuery.get(input.greetingId());

        return new GetGreeting.Output(greeting.greetingId(),
                                      greeting.greetingTitle(),
                                      greeting.greetingDetail(),
                                      greeting.createdDate());
    }

}
