package com.thitsaworks.operation_portal.core.test_iam.command;

import com.aerospike.client.ResultCode;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface BlockUserActionCommand {
    Output execute(Input input) throws IAMException;

    record Input(
        UserId userId,
        ActionId actionId) { }

    record Output(boolean resultCode) { }

}
