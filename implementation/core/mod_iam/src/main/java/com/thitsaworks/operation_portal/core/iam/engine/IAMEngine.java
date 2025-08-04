package com.thitsaworks.operation_portal.core.iam.engine;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;

import java.util.Map;
import java.util.Set;

public interface IAMEngine {

    void bootstrap();

    void print();

    boolean isGrantedAction(PrincipalId principalId, ActionCode actionCode);

    boolean isGrantedAction(PrincipalId principalId, ActionId actionId);

    // Map accessor methods for listeners
    Map<PrincipalId, PrincipalData> getPrincipalsMap();

    Map<PrincipalId, Set<RoleData>> getPrincipalRolesMap();

    Map<ActionCode, ActionData> getActionCodesMap();

    Map<ActionId, ActionData> getActionIdsMap();

    Map<RoleId, Set<ActionData>> getRoleGrantedActionsMap();

    Map<PrincipalId, Set<ActionData>> getPrincipalGrantedActionsMap();

    Map<PrincipalId, Set<ActionData>> getPrincipalDeniedActionsMap();

}
