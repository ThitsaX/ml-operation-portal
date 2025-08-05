package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.core.test_iam.command.GrantMenuActionByIdCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.Menu;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrantMenuActionByIdCommandHandler implements GrantMenuActionByIdCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionByIdCommandHandler.class);

    private final MenuRepository menuRepository;

    private final IAMActionRepository iamActionRepository;

    @Override
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findById(input.menuId());

        if (optMenu.isEmpty()) {

            LOG.info("Menu Not Found : [{}]", input.menuId());
            throw new IAMException(IAMErrors.MENU_NOT_FOUND);
        }

        Optional<IAMAction> optAction = this.iamActionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {

            LOG.info("Action Not Found : [{}]", input.actionId());
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        Menu menu = optMenu.get();

        menu.grantAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
