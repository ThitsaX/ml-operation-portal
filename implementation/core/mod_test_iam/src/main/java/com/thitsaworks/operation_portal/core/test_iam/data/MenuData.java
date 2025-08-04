package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.model.Menu;

public record MenuData(MenuId menuId,
                       String name,
                       String parentId,
                       boolean isActive) {

    public MenuData(Menu menu){
        this(menu.getMenuId(),
             menu.getName(),
             menu.getParentId(),
             menu.isActive());
    }

}
