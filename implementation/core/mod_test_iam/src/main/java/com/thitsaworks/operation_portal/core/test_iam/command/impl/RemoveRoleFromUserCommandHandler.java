package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.RemoveRoleFromUserCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveRoleFromUserCommandHandler implements RemoveRoleFromUserCommand {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {


        User user = this.userRepository.findById(input.userId())
                                       .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        Role role = this.roleRepository.findById(input.roleId())
                                       .orElseThrow(() -> new IAMException(IAMErrors.ROLE_ID_NOT_FOUND));

        var removed = user.removeRole(role);

        this.userRepository.save(user);

        return new Output(removed);
    }

}
