package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.GrantMenuActionCommand;
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
public class GrantMenuActionCommandHandler implements GrantMenuActionCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionCommandHandler.class);

    private final MenuRepository menuRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findOne(MenuRepository.Filters.withName(input.menuName()));

        if (optMenu.isEmpty()) {

            LOG.info("Menu Not Found : [{}]", input.menuName());
            throw new IAMException(IAMErrors.MENU_NOT_FOUND.format(input.menuName()));
        }

        Optional<Action> optAction = this.actionRepository.findOne(ActionRepository.Filters.withActionCode(input.action()));

        if (optAction.isEmpty()) {

            LOG.info("Action Not Found : [{}]", input.action());
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND.format(input.action()));
        }

        Menu menu = optMenu.get();
        menu.grantAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
