package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantMenuActionCommand;
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

import static com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository.Filters.withActionCode;

@Service
@RequiredArgsConstructor
public class GrantMenuActionCommandHandler implements GrantMenuActionCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionCommandHandler.class);

    private final MenuRepository menuRepository;

    private final IAMActionRepository iamActionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findOne(MenuRepository.Filters.withName(input.menuName()));

        if (optMenu.isEmpty()) {

            LOG.info("Menu Not Found : [{}]", input.menuName());
            throw new IAMException(IAMErrors.MENU_NOT_FOUND);
        }

        Optional<IAMAction> optAction = this.iamActionRepository.findOne(withActionCode(input.action()));

        if (optAction.isEmpty()) {

            LOG.info("Action Not Found : [{}]", input.action());
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        Menu menu = optMenu.get();

        menu.grantAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
