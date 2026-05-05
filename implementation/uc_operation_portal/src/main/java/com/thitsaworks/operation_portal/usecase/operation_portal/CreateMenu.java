package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateMenu extends UseCase<CreateMenu.Input, CreateMenu.Output> {

    record Input(MenuId menuId, String menuName, String parentId, boolean isActive) { }

    record Output(MenuId menuId) { }

}