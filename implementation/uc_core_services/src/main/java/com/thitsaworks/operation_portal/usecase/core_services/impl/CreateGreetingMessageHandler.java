package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.CoreServicesUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.CreateGreetingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class CreateGreetingMessageHandler
    extends CoreServicesUseCase<CreateGreetingMessage.Input, CreateGreetingMessage.Output>
    implements CreateGreetingMessage {

    private static final Logger LOGGER= LoggerFactory.getLogger(CreateGreetingMessageHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final CreateGreetingMessageCommand createGreetingMessageCommand;

    public CreateGreetingMessageHandler(PrincipalCache principalCache, CreateGreetingMessageCommand createGreetingMessageCommand) {

        super(PERMITTED_ROLES, principalCache);
        this.createGreetingMessageCommand = createGreetingMessageCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output= this.createGreetingMessageCommand.execute(new CreateGreetingMessageCommand.Input(input.greetingTitle(),
                                                                                                     input.greetingDetail(),
                                                                                                     input.greetingDate()));

        return new CreateGreetingMessage.Output(output.created());

    }

}
