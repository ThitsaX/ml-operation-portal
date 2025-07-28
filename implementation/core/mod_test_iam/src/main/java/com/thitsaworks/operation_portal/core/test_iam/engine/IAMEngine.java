package com.thitsaworks.operation_portal.core.test_iam.engine;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

import java.util.List;

public interface IAMEngine {

    void bootstrap();

    List<ActionData> getActions();

    List<RoleData> getRoles();

    List<UserData> getUsers();

    List<ActionData> getActionsByRole(String role) throws IAMException;

    boolean isGranted(UserId userId, ActionCode actionCode) throws IAMException;

}
