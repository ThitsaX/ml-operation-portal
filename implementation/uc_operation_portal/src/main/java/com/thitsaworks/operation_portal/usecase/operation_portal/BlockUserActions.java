package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface BlockUserActions extends UseCase<BlockUserActions.Input, BlockUserActions.Output> {

    record Input(UserId userId,
                 List<ActionId> actionIdList) { }

    record Output(boolean blocked) { }

}
