package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantRoleActionByIdCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrantRoleActionByIdCommandHandler implements GrantRoleActionByIdCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionByIdCommandHandler.class);

    private final RoleRepository roleRepository;

    private final IAMActionRepository IAMActionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Role> optRole = this.roleRepository.findById(input.roleId());

        if (optRole.isEmpty()) {

            LOG.info("Role Not Found : [{}]", input.roleId());
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND);
        }

        var role = optRole.get();

        Optional<IAMAction> optAction = this.IAMActionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {

            LOG.info("Action Not Found : [{}]", input.actionId());
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        role.grantAction(optAction.get());

        this.roleRepository.saveAndFlush(role);

        return new Output(true);
    }

}
