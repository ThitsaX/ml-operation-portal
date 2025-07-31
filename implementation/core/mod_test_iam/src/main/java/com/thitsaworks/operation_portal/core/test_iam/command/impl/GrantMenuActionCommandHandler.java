package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantMenuActionCommad;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.Menu;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrantMenuActionCommandHandler implements GrantMenuActionCommad {

    private final MenuRepository menuRepository;

    private final IAMActionRepository iamActionRepository;
    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {
        Optional<Menu> optMenu = this.menuRepository.findById(input.menuId());

        if (optMenu.isEmpty()) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);
        }

        Optional<IAMAction> optAction = this.iamActionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        Menu menu = optMenu.get();
        menu.grantAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
