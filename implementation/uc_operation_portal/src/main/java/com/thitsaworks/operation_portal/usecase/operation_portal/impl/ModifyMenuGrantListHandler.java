package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyMenuGrantListCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyMenuGrantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class ModifyMenuGrantListHandler
    extends OperationPortalUseCase<ModifyMenuGrantList.Input, ModifyMenuGrantList.Output>
    implements ModifyMenuGrantList {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyMenuGrantListHandler.class);

    private final ModifyMenuGrantListCommand modifyMenuGrantListCommand;

    public ModifyMenuGrantListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      ModifyMenuGrantListCommand modifyMenuGrantListCommand) {

        super(principalCache, actionAuthorizationManager);

        this.modifyMenuGrantListCommand = modifyMenuGrantListCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.modifyMenuGrantListCommand
                         .execute(new ModifyMenuGrantListCommand.Input(
                             input.menuId(),
                             input.actionIdList()))
                         .modified();

        return new Output(output);
    }

}
