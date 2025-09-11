package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.RevokeMenuActionCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Menu;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevokeMenuActionCommandHandler implements RevokeMenuActionCommand {

    private final MenuRepository menuRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Menu> optMenu = this.menuRepository.findById(input.menuId());

        if (optMenu.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND);
        }

        Optional<Action> optAction = this.actionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND.defaultMessage(
                    "System cannot find Action [" + input.actionId() + "]"));
        }

        Menu menu = optMenu.get();
        var revoked = menu.revokeAction(optAction.get());

        this.menuRepository.saveAndFlush(menu);

        return new Output(revoked);
    }

}
