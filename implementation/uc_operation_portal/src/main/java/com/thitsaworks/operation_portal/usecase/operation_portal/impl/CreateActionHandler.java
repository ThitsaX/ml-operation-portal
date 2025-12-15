package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateAction;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class CreateActionHandler extends OperationPortalUseCase<CreateAction.Input, CreateAction.Output>
    implements CreateAction {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionHandler.class);

    private final CreateOrUpdateActionCommand createOrUpdateActionCommand;

    public CreateActionHandler(PrincipalCache principalCache,
                               ActionAuthorizationManager actionAuthorizationManager,
                               CreateOrUpdateActionCommand createOrUpdateActionCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.createOrUpdateActionCommand = createOrUpdateActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.createOrUpdateActionCommand.execute(new CreateOrUpdateActionCommand.Input(input.actionCode(),
                                                                                                    input.scope(),
                                                                                                    input.description()));

        return new Output(output.actionId());
    }

}
