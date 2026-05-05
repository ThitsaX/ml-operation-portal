package com.thitsaworks.operation_portal.core.iam.query;


import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;

public interface RoleQuery {

    RoleData get(String name) throws IAMException;

    List<RoleData> getAll() throws IAMException;

    RoleData get(RoleId roleId) throws IAMException;


}
