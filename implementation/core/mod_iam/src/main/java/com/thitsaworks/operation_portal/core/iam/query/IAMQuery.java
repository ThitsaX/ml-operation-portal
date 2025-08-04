package com.thitsaworks.operation_portal.core.iam.query;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;

public interface IAMQuery {

    List<RoleData> getRoles();

    List<PrincipalData> getPrincipals();

    List<ActionData> getActions();

    List<ActionData> getActionsByRole(String role) throws IAMException;

}
