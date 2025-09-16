package com.thitsaworks.operation_portal.core.iam.query;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.MenuData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;
import java.util.Map;

public interface IAMQuery {

    List<RoleData> getRoles();

    List<PrincipalData> getPrincipals();

    List<ActionData> getActions();

    List<RoleData> getRolesByPrincipal(PrincipalId principalId) throws IAMException;

    Map<List<MenuData>, List<ActionData>> getMenusAndActionsByUserId(PrincipalId principalId) throws IAMException;

    List<ActionData> getGrantedActionsByPrincipal(PrincipalId principalId) throws IAMException;

}
