package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.participant.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateGreetingMessage;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateGreetingMessageHandler
    extends OperationPortalUseCase<CreateGreetingMessage.Input, CreateGreetingMessage.Output>
    implements CreateGreetingMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGreetingMessageHandler.class);

    private final CreateGreetingMessageCommand createGreetingMessageCommand;

    public CreateGreetingMessageHandler(PrincipalCache principalCache,
                                        CreateGreetingMessageCommand createGreetingMessageCommand,
                                        ActionAuthorizationManager actionAuthorizationManager) {

        super(principalCache, actionAuthorizationManager);

        this.createGreetingMessageCommand = createGreetingMessageCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.createGreetingMessageCommand.execute(new CreateGreetingMessageCommand.Input(input.greetingTitle(),
                                                                                             input.greetingDetail(),
                                                                                             input.greetingDate()));

        return new CreateGreetingMessage.Output(output.created());

    }

}
