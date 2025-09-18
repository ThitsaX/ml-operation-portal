package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.data.MenuData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Menu;
import com.thitsaworks.operation_portal.core.iam.model.QMenu;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
import com.thitsaworks.operation_portal.core.iam.query.MenuQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuJpaQueryHandler implements MenuQuery {

    private final QMenu menu = QMenu.menu;

    private final MenuRepository menuRepository;

    @Override
    public List<MenuData> getAll() throws IAMException {

        BooleanExpression predicate = this.menu.menuId.isNotNull();

        var menus = (List<Menu>) this.menuRepository.findAll(predicate);

        return menus.stream()
                    .map(MenuData::new)
                    .toList();
    }

    @Override
    public MenuData get(MenuId menuId) throws IAMException {

        BooleanExpression predicate = this.menu.menuId.eq(menuId);

        var menu = this.menuRepository.findOne(predicate);

        if (menu.isEmpty()) {
            throw new IAMException(IAMErrors.MENU_NOT_FOUND.format(menuId.getId().toString()));
        }

        return new MenuData(menu.get());
    }

}
