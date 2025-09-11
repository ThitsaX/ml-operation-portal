package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.AssignRoleToPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignRoleToPrincipalCommandHandler implements AssignRoleToPrincipalCommand {

    private final PrincipalRepository principalRepository;

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Principal principal = this.principalRepository.findById(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.defaultMessage(
                                                              "Principal is not found for the user [" +
                                                                      input.principalId().getId() + "].")));

        Role role = this.roleRepository.findById(input.roleId())
                                       .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));

        PrincipalRole principalRole = principal.assignRole(role);

        this.principalRepository.saveAndFlush(principal);

        return new Output(principalRole.getPrincipalRoleId());
    }

}

