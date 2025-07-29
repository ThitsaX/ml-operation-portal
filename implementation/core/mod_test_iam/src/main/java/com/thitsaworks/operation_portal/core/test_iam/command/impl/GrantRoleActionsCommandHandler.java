package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.GrantRoleActionsCommand;
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

import static com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository.Filters.withActionCode;

@Service
@RequiredArgsConstructor
public class GrantRoleActionsCommandHandler implements GrantRoleActionsCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionsCommandHandler.class);

    private final RoleRepository roleRepository;

    private final IAMActionRepository IAMActionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Role> optRole = this.roleRepository.findOne(RoleRepository.Filters.withName(input.role()));

        if (optRole.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND);
        }

        var role = optRole.get();

        for (var action : input.actionCodeList()) {
            Optional<IAMAction> optAction = this.IAMActionRepository.findOne(withActionCode(action));

            if (optAction.isEmpty()) {

                LOG.info("Action Not Found : [{}]", action);
                throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
            }

            role.grantAction(optAction.get());
        }

        this.roleRepository.saveAndFlush(role);

        return new Output(true);
    }

}
