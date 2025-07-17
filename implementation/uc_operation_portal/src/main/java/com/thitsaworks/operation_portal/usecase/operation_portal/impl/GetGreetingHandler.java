package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.query.GreetingQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;

import com.thitsaworks.operation_portal.usecase.operation_portal.GetGreeting;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetGreetingHandler extends OperationPortalAuditableUseCase<GetGreeting.Input, GetGreeting.Output>
    implements GetGreeting {

    private  final GreetingQuery greetingQuery;

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

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
                                      greeting.isDeleted());
    }

}
