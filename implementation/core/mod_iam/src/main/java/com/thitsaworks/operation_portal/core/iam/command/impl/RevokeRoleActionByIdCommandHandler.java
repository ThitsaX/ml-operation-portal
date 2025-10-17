package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.RevokeRoleActionByIdCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevokeRoleActionByIdCommandHandler implements RevokeRoleActionByIdCommand {

    private final RoleRepository roleRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<Role> optRole = this.roleRepository.findById(input.roleId());

        if (optRole.isEmpty()) {
            throw new IAMException(IAMErrors.ROLE_NOT_FOUND.format(input.roleId()
                                                                        .getId()
                                                                        .toString()));
        }

        Optional<Action> optAction = this.actionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND.format(input.actionId()
                                                                          .getId()
                                                                          .toString()));
        }

        Role role = optRole.get();
        var revoked = role.revokeAction(optAction.get());

        this.roleRepository.saveAndFlush(role);

        return new Output(revoked);
    }

}
