package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.model.Menu;

import java.util.Objects;

public record MenuData(MenuId menuId,
                       String name,
                       String parentId,
                       boolean isActive) {

    public MenuData(Menu menu) {

        this(menu.getMenuId(),
             menu.getName(),
             menu.getParentId(),
             menu.isActive());
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuData that = (MenuData) o;
        return Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(menuId);
    }


}
