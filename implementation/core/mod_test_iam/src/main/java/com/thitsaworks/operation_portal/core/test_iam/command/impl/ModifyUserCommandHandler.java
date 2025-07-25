package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.ModifyUserCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyUserCommandHandler implements ModifyUserCommand {

    private  final UserRepository userRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var user = this.userRepository.findByUserId(input.userId())
                                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        this.userRepository.save(user.modify(input.principalStatus(), input.userRoleType()));

        return new Output(user.getUserId(), true);
    }

}
