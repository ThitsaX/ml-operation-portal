package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.RemoveRoleFromPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveRoleFromPrincipalCommandHandler implements RemoveRoleFromPrincipalCommand {

    private final PrincipalRepository principalRepository;

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Principal principal = this.principalRepository.findById(input.principalId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.defaultMessage(
                                                              "Principal is not found for the user [" + input.principalId().getId() + "].")));

        Role role = this.roleRepository.findById(input.roleId())
                                       .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND.defaultMessage(
                                               "System cannot find [" + input.roleId() + "] Role.")));

        var removed = principal.removeRole(role);

        this.principalRepository.save(principal);

        return new Output(removed);
    }

}
