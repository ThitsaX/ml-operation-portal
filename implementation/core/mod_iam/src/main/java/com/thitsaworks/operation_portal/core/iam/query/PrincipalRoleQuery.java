package com.thitsaworks.operation_portal.core.iam.query;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalRoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;


public interface PrincipalRoleQuery {

    PrincipalRoleData getRole(PrincipalId principalId) throws IAMException;


}
