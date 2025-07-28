package com.thitsaworks.operation_portal.core.test_iam.engine.impl;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.command.IsActionGrantedCommand;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.query.ActionQuery;
import com.thitsaworks.operation_portal.core.test_iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.test_iam.query.RoleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationPortalIAMEngine implements IAMEngine {

    private final IAMQuery iamQuery;

    private final ActionQuery actionQuery;

    private final RoleQuery roleQuery;

    private final IsActionGrantedCommand isActionGrantedCommand;

    @Override
    public void bootstrap() {

    }

    @Override
    public List<ActionData> getActions() {

        return this.iamQuery.getActions();
    }

    @Override
    public List<RoleData> getRoles() {

        return this.iamQuery.getRoles();
    }

    @Override
    public List<UserData> getUsers() {

        return this.iamQuery.getUsers();
    }

    @Override
    public List<ActionData> getActionsByRole(String role) throws IAMException {

        var
            roleId =
            this.roleQuery.get(role)
                          .roleId();

        return this.iamQuery.getActionsByRole(roleId);
    }

    @Override
    public boolean isGranted(UserId userId, ActionCode actionCode) throws IAMException {

        var
            actionId =
            this.actionQuery.get(actionCode)
                            .actionId();

        return this.isActionGrantedCommand.execute(new IsActionGrantedCommand.Input(userId, actionId))
                                          .granted();
    }

}
