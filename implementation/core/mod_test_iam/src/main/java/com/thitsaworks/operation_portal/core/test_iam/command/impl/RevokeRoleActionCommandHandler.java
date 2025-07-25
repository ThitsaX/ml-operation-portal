package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.RevokeRoleActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Action;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevokeRoleActionCommandHandler implements RevokeRoleActionCommand {

    private final RoleRepository roleRepository;

    private final ActionRepository actionRepository;
    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {
        Optional<Role> optRole = this.roleRepository.findById(input.roleId());

        if (optRole.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_ID_NOT_FOUND);
        }

        Optional<Action> optAction = this.actionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_ID_NOT_FOUND);
        }

        Role role = optRole.get();
        var revoked = role.revokeAction(optAction.get());
        this.roleRepository.saveAndFlush(role);

        return new Output(revoked);
    }

}
