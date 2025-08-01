package com.thitsaworks.operation_portal.usecase.util.action;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionAuthorizationManager {

    private final CreateOrUpdateActionCommand createOrUpdateActionCommand;

    private final IAMEngine iamEngine;

    @Autowired
    public ActionAuthorizationManager(CreateOrUpdateActionCommand createOrUpdateActionCommand,
                                      IAMEngine iamEngine) {

        this.createOrUpdateActionCommand = createOrUpdateActionCommand;
        this.iamEngine = iamEngine;
    }

    public void registerAction(String actionName, String scope, String description) {

        var input = new CreateOrUpdateActionCommand.Input(
            new ActionCode(actionName),
            scope,
            description
        );

        this.createOrUpdateActionCommand.execute(input);
    }

    public boolean isAuthorizedTo(UserId userId, ActionCode actionCode) throws IAMException {

        return this.iamEngine.isGrantedAction(userId, actionCode);
    }

}
