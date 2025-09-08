package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.participant.command.RemoveGreetingCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.RemovePastSixMonthsGreetingMessage;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Set;

@Service
public class RemovePastSixMonthsGreetingHandler
    extends OperationPortalUseCase<RemovePastSixMonthsGreetingMessage.Input, RemovePastSixMonthsGreetingMessage.Output>
    implements RemovePastSixMonthsGreetingMessage {

    private final RemoveGreetingCommand removeGreetingCommand;

    public RemovePastSixMonthsGreetingHandler(Set<UserRoleType> permittedRoles,
                                              PrincipalCache principalCache,
                                              ActionAuthorizationManager actionAuthorizationManager,
                                              RemoveGreetingCommand removeGreetingCommand) {

        super(principalCache, actionAuthorizationManager);

        this.removeGreetingCommand = removeGreetingCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.removeGreetingCommand.execute(new RemoveGreetingCommand.Input());

        return new Output(output.removed());
    }

}
