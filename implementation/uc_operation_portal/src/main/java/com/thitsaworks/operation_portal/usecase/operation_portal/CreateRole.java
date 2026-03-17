package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateRole extends UseCase<CreateRole.Input, CreateRole.Output> {

    record Input(String name, boolean isDfsp) { }

    record Output(RoleId roleId) { }

}
