package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface ModifyMenuGrantList
    extends UseCase<ModifyMenuGrantList.Input, ModifyMenuGrantList.Output> {

    record Input(MenuId menuId, List<ActionId> actionIdList) { }

    record Output(boolean modified) { }

}
