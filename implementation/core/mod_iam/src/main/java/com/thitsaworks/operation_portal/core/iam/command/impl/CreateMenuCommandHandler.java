package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.CreateMenuCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Menu;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateMenuCommandHandler implements CreateMenuCommand {

    private final MenuRepository menuRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findOne(
            MenuRepository.Filters.withName(input.name()));

        if (optMenu.isPresent()) {

            throw new IAMException(IAMErrors.DUPLICATE_MENU_NAME.format(input.name()));
        }

        var menu = new Menu(input.menuId(), input.name(), input.parentId(), input.isActive());

        this.menuRepository.save(menu);

        return new Output(menu.getMenuId());

    }

}

