package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.EditRoleCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditRoleCommandHandler implements EditRoleCommand {

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var role = this.roleRepository.findById(input.roleId())
                                      .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND.format(input.roleId()
                                                                                                               .getId().toString())));

        if (this.roleRepository.findOne(RoleRepository.Filters.withName(input.name())
                                                              .and(RoleRepository.Filters.withoutRoleId(input.roleId())))
                               .isPresent()) {
            throw new IAMException(IAMErrors.DUPLICATE_ROLE_NAME.format(input.name()));
        }

        role.name(input.name());

        this.roleRepository.saveAndFlush(role);

        return new Output(true);
    }

}
