package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.RevokeRoleActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevokeRoleActionCommandHandler implements RevokeRoleActionCommand {

    private final RoleRepository roleRepository;

    private final IAMActionRepository IAMActionRepository;
    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {
        Optional<Role> optRole = this.roleRepository.findById(input.roleId());

        if (optRole.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND);
        }

        Optional<IAMAction> optAction = this.IAMActionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        Role role = optRole.get();
        var revoked = role.revokeAction(optAction.get());
        this.roleRepository.saveAndFlush(role);

        return new Output(revoked);
    }

}
