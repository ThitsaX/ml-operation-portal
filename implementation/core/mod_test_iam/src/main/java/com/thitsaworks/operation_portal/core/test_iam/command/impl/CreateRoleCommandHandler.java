package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateRoleCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateRoleCommandHandler implements CreateRoleCommand {

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {
        Optional<Role> optRole = this.roleRepository.findOne(RoleRepository.Filters.withName(input.name()));

        if (optRole.isPresent()) {

            throw new IAMException(IAMErrors.DUPLICATE_ROLE_NAME);
        }

        var role = new Role(input.name());

        this.roleRepository.saveAndFlush(role);

        return new Output(role.getRoleId());
    }

}
