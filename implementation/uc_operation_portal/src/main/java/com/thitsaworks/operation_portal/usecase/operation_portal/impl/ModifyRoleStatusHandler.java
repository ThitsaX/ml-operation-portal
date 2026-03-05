package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyRoleStatusCommand;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyRoleStatus;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class ModifyRoleStatusHandler
    extends OperationPortalUseCase<ModifyRoleStatus.Input, ModifyRoleStatus.Output>
    implements ModifyRoleStatus {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyRoleStatusHandler.class);

    private final ModifyRoleStatusCommand modifyRoleStatusCommand;

    private final IAMEngine iamEngine;

    public ModifyRoleStatusHandler(PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   ModifyRoleStatusCommand modifyRoleStatusCommand,
                                   IAMEngine iamEngine) {

        super(principalCache, actionAuthorizationManager);

        this.modifyRoleStatusCommand = modifyRoleStatusCommand;
        this.iamEngine = iamEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.modifyRoleStatusCommand.execute(new ModifyRoleStatusCommand.Input(input.roleId(),
                                                                                             input.active()));

        this.iamEngine.bootstrap();

        return new Output(output.modified());
    }

}
