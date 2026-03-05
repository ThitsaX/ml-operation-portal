package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.ModifyRoleStatusCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyRoleStatusCommandHandler implements ModifyRoleStatusCommand {

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var role = this.roleRepository.findById(input.roleId())
                                      .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND.format(input.roleId()
                                                                                                               .getId().toString())));

        if (role.getActive() != input.active()) {
            role.toggle();
        }

        this.roleRepository.saveAndFlush(role);

        return new Output(role.getRoleId(), true);
    }

}
