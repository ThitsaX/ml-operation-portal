package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantMenuActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantMenuActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GrantMenuActionListHandler
    extends OperationPortalUseCase<GrantMenuActionList.Input, GrantMenuActionList.Output>
    implements GrantMenuActionList {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionListHandler.class);

    private final GrantMenuActionCommand grantMenuActionCommand;

    public GrantMenuActionListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      GrantMenuActionCommand grantMenuActionCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.grantMenuActionCommand = grantMenuActionCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        for (var menu : input.menuGrantList()) {

            for (var action : menu.actionList()) {
                this.grantMenuActionCommand.execute(new GrantMenuActionCommand.Input(menu.menuName(), action));
            }
        }

        return new Output(true);
    }

}
