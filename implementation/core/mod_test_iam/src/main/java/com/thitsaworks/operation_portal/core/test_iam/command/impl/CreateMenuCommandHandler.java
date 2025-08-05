package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateMenuCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Menu;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMenuCommandHandler implements CreateMenuCommand {


    private MenuRepository menuRepository;


    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var menu = new Menu(input.name(),
                            input.parentId(),
                            input.isActive());
        this.menuRepository.save(menu);


        return new Output(menu.getMenuId());

    }

}

