package com.thitsaworks.operation_portal.core.test_iam.command;

import com.aerospike.client.ResultCode;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface EditRoleCommand {

    Output execute(Input input) throws IAMException;

    record Input(RoleId roleId, String name){}

    record Output(boolean resultCode){}
}

