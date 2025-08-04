package com.thitsaworks.operation_portal.core.iam.query;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.data.MenuData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;

public interface MenuQuery {

    List<MenuData> getAll() throws IAMException;

    MenuData get(MenuId menuId) throws IAMException;

}
