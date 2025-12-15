package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.BlockPrincipalActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.BlockUserActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class BlockUserActionListHandler
    extends OperationPortalUseCase<BlockUserActionList.Input, BlockUserActionList.Output>
    implements BlockUserActionList {

    private static final Logger LOG = LoggerFactory.getLogger(BlockUserActionListHandler.class);

    private final BlockPrincipalActionCommand blockPrincipalActionCommand;

    public BlockUserActionListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      BlockPrincipalActionCommand blockprincipalActionCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.blockPrincipalActionCommand = blockprincipalActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var actionId : input.actionIdList()) {
            this.blockPrincipalActionCommand.execute(new BlockPrincipalActionCommand.Input(input.principalId(),
                                                                                           actionId));
        }

        return new Output(true);
    }

}
