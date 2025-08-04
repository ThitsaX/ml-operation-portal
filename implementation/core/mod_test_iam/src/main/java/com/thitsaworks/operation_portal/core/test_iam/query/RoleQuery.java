package com.thitsaworks.operation_portal.core.test_iam.query;

import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

import java.util.List;

public interface RoleQuery {

    RoleData get(String name) throws IAMException;

    List<RoleData> getAll() throws IAMException;
}
