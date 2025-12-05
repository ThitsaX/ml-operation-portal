package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;

import java.util.Map;
import java.util.Set;

public record EngineData(Map<PrincipalId, PrincipalData> principalsMap,
                         Map<PrincipalId, Set<RoleData>> principalRolesMap,
                         Map<ActionCode, ActionData> actionCodesMap,
                         Map<ActionId, ActionData> actionIdsMap,
                         Map<RoleId, Set<ActionData>> roleGrantedActionsMap,
                         Map<PrincipalId, Set<ActionData>> principalGrantedActionsMap,
                         Map<PrincipalId, Set<ActionData>> principalDeniedActionsMap) {
}
