package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyMenuGrantListCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifyMenuGrantListCommandHandler implements ModifyMenuGrantListCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyMenuGrantListCommandHandler.class);

    private final MenuRepository menuRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var menu = this.menuRepository.findById(input.menuId()).orElseThrow(() -> {
            LOG.info("Menu Not Found : [{}]", input.menuId());
            return new IAMException(
                IAMErrors.MENU_NOT_FOUND.format(input.menuId().getId().toString()));
        });

        List<Action> actionList = new ArrayList<>();
        var actionIdList = input.actionIdList() == null
            ? List.<ActionId>of()
            : new ArrayList<>(new LinkedHashSet<>(input.actionIdList()));

        for (var actionId : actionIdList) {

            var action = this.actionRepository.findById(actionId).orElseThrow(() -> {
                LOG.info("Action Not Found : [{}]", actionId);
                return new IAMException(
                    IAMErrors.ACTION_NOT_FOUND.format(actionId.getId().toString()));
            });

            actionList.add(action);
        }

        menu.grantActions(actionList);

        this.menuRepository.saveAndFlush(menu);

        return new Output(true);
    }

}
