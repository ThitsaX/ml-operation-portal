package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateGreeting;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

@Service
public class UpdateGreetingHandler extends OperationPortalUseCase<UpdateGreeting.Input, UpdateGreeting.Output>
    implements UpdateGreeting {

    private final UpdateGreetingCommand updateGreetingCommand;

    public UpdateGreetingHandler(PrincipalCache principalCache,
                                 ActionAuthorizationManager actionAuthorizationManager,
                                 UpdateGreetingCommand updateGreetingCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.updateGreetingCommand = updateGreetingCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.updateGreetingCommand.execute(new UpdateGreetingCommand.Input(input.greetingId(),
                                                                                        input.greetingTitle(),
                                                                                        input.greetingDetail(),
                                                                                        input.isDeleted(),
                                                                                        input.greetingDate()));

        return new Output(output.greetingId());
    }

}
