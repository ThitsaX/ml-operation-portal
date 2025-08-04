package com.thitsaworks.operation_portal.core.test_iam.query;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface ActionQuery {

    ActionData get(ActionCode actionCode) throws IAMException;

}
