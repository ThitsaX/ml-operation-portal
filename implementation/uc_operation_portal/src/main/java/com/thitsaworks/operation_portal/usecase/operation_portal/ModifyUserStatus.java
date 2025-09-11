package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyUserStatus extends UseCase<ModifyUserStatus.Input, ModifyUserStatus.Output> {

    record Input(UserId userId,
                 PrincipalStatus activeStatus) { }

    record Output(boolean removed, UserId userId) { }

}
