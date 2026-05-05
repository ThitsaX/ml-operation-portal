package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreateMenuCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateMenu;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class CreateMenuHandler extends OperationPortalUseCase<CreateMenu.Input, CreateMenu.Output>
    implements CreateMenu {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMenuHandler.class);

    private final CreateMenuCommand createMenuCommand;

    public CreateMenuHandler(PrincipalCache principalCache,
                             ActionAuthorizationManager actionAuthorizationManager,
                             CreateMenuCommand createMenuCommand) {

        super(principalCache, actionAuthorizationManager);

        this.createMenuCommand = createMenuCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.createMenuCommand.execute(
            new CreateMenuCommand.Input(
                input.menuId(), input.menuName(), input.parentId(),
                input.isActive()));

        return new Output(output.menuId());
    }

}
