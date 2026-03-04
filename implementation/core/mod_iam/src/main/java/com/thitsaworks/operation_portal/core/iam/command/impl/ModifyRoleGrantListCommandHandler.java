package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyRoleGrantListCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifyRoleGrantListCommandHandler implements ModifyRoleGrantListCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyRoleGrantListCommandHandler.class);

    private final RoleRepository roleRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var role = this.roleRepository.findById(input.roleId()).orElseThrow(() -> {
            LOG.info("Role Not Found : [{}]", input.roleId());
            return new IAMException(
                IAMErrors.ROLE_NOT_FOUND.format(input.roleId().getId().toString()));
        });

        List<Action> actionList = new ArrayList<>();

        for (var actionId : input.actionIdList()) {

            var action = this.actionRepository.findById(actionId).orElseThrow(() -> {
                LOG.info("Action Not Found : [{}]", actionId);
                return new IAMException(
                    IAMErrors.ACTION_NOT_FOUND.format(actionId.getId().toString()));
            });

            actionList.add(action);

        }

        role.grantActions(actionList);

        this.roleRepository.saveAndFlush(role);

        return new Output(true);
    }

}
