package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyRoleGrantListCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyRoleGrantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class ModifyRoleGrantListHandler
    extends OperationPortalUseCase<ModifyRoleGrantList.Input, ModifyRoleGrantList.Output>
    implements ModifyRoleGrantList {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyRoleGrantListHandler.class);

    private final ModifyRoleGrantListCommand modifyRoleGrantListCommand;

    public ModifyRoleGrantListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      ModifyRoleGrantListCommand modifyRoleGrantListCommand) {

        super(principalCache, actionAuthorizationManager);

        this.modifyRoleGrantListCommand = modifyRoleGrantListCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.modifyRoleGrantListCommand
                         .execute(new ModifyRoleGrantListCommand.Input(
                             input.roleId(),
                             input.actionIdList()))
                         .modified();

        return new Output(output);
    }

}
