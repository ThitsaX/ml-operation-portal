package com.thitsaworks.operation_portal.core.test_iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.data.MenuData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.QMenu;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.MenuRepository;
import com.thitsaworks.operation_portal.core.test_iam.query.MenuQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuJpaQueryHandler implements MenuQuery {

    private final QMenu menu =QMenu.menu;

    private final MenuRepository menuRepository;

    @Override
    public List<MenuData> getAll() throws IAMException {
     return  null;
    }


    @Override
    public MenuData get(MenuId menuId) throws IAMException {
        BooleanExpression predicate = this.menu.menuId.eq(menuId);

        var menu = this.menuRepository.findOne(predicate);

        if (menu.isEmpty()) {
            throw new IAMException(IAMErrors.MENU_NOT_FOUND);
        }

        return new MenuData(menu.get());
    }

}
