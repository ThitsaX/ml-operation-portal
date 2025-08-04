package com.thitsaworks.operation_portal.core.iam.query;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface ActionQuery {

    ActionData get(ActionCode actionCode) throws IAMException;

}
