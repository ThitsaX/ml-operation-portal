package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.command.IsActionGrantedCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionAuthorizationManager {

    private final CreateOrUpdateActionCommand createOrUpdateActionCommand;

    private final IsActionGrantedCommand isActionGrantedCommand;

    @Autowired
    public ActionAuthorizationManager(CreateOrUpdateActionCommand createOrUpdateActionCommand,
                                      IsActionGrantedCommand isActionGrantedCommand) {

        this.createOrUpdateActionCommand = createOrUpdateActionCommand;
        this.isActionGrantedCommand = isActionGrantedCommand;
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

        return this.isActionGrantedCommand.execute(new IsActionGrantedCommand.Input(userId, actionCode)).granted();
    }

}
