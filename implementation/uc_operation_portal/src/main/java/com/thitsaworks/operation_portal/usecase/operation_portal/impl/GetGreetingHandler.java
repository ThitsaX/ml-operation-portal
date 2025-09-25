package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.GreetingQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetGreeting;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetGreetingHandler extends OperationPortalUseCase<GetGreeting.Input, GetGreeting.Output>
    implements GetGreeting {

    private static final Logger LOG = LoggerFactory.getLogger(GetGreetingHandler.class);

    private final GreetingQuery greetingQuery;

    public GetGreetingHandler(PrincipalCache principalCache,
                              ActionAuthorizationManager actionAuthorizationManager,
                              GreetingQuery greetingQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.greetingQuery = greetingQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        return this.greetingQuery.getLatestGreeting()
                .map(greeting -> new Output(
                        greeting.greetingId(),
                        greeting.greetingTitle(),
                        greeting.greetingDetail(),
                        greeting.isDeleted()
                )).orElse(null);
    }

}
