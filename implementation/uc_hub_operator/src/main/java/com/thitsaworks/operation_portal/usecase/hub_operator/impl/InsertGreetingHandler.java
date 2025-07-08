package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.home_message.command.InsertGreetingCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.InsertGreeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InsertGreetingHandler extends HubOperatorUseCase<InsertGreeting.Input,InsertGreeting.Output> implements InsertGreeting {

    private static final Logger LOGGER= LoggerFactory.getLogger(InsertGreetingHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN);

    private final InsertGreetingCommand insertGreetingCommand;

    public InsertGreetingHandler(PrincipalCache principalCache, InsertGreetingCommand insertGreetingCommand) {

        super(PERMITTED_ROLES, principalCache);
        this.insertGreetingCommand = insertGreetingCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output= this.insertGreetingCommand.execute(new InsertGreetingCommand.Input(input.greetingTitle(),
                                                                                       input.greetingDetail()));

        return new InsertGreeting.Output(output.created());

    }

}
