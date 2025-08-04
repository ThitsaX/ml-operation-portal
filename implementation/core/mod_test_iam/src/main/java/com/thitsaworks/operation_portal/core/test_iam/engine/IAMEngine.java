package com.thitsaworks.operation_portal.core.test_iam.engine;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;

import java.util.Map;
import java.util.Set;

public interface IAMEngine {

    void bootstrap();

    void print();

    boolean isGrantedAction(UserId userId, ActionCode actionCode);

    boolean isGrantedAction(UserId userId, ActionId actionId);

    // Map accessor methods for listeners
    Map<UserId, UserData> getUsersMap();

    Map<UserId, Set<RoleData>> getUserRolesMap();

    Map<ActionCode, ActionData> getActionCodesMap();

    Map<ActionId, ActionData> getActionIdsMap();

    Map<RoleId, Set<ActionData>> getRoleGrantedActionsMap();

    Map<UserId, Set<ActionData>> getUserGrantedActionsMap();

    Map<UserId, Set<ActionData>> getUserDeniedActionsMap();

}
