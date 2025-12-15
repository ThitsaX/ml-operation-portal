package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.RevokeMenuActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RevokeMenuActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RevokeMenuActionListHandler
    extends OperationPortalUseCase<RevokeMenuActionList.Input, RevokeMenuActionList.Output>
    implements RevokeMenuActionList {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeMenuActionListHandler.class);

    private final RevokeMenuActionCommand revokeMenuActionCommand;

    public RevokeMenuActionListHandler(PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       RevokeMenuActionCommand revokeMenuActionCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.revokeMenuActionCommand = revokeMenuActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var actionCode : input.actionCodeList()) {

            this.revokeMenuActionCommand.execute(new RevokeMenuActionCommand.Input(input.menuName(), actionCode));
        }

        return new Output(true);
    }

}
