package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.GrantMenuActionByIdCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Menu;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
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

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findById(input.menuId());

        if (optMenu.isEmpty()) {

            LOG.info("Menu Not Found : [{}]", input.menuId());
            throw new IAMException(IAMErrors.MENU_NOT_FOUND.format(input.menuId()));
        }

        Optional<Action> optAction = this.actionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {

            LOG.info("Action Not Found : [{}]", input.actionId());
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND.format(input.actionId()));
        }

        Menu menu = optMenu.get();
        menu.grantAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
