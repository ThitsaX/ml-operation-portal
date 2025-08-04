package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.ChangeUserPasswordCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeUserPasswordCommandHandler implements ChangeUserPasswordCommand {

    private final UserRepository userRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {
        var user = this.userRepository.findById(input.userId())
                                      .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        AccessKey oldAccessKey = user.getAccessKey();

        user.change(input.currentPassword(), input.newPassword());

        this.userRepository.saveAndFlush(user);



        return new Output(user.getAccessKey(),user.getSecretKey());
    }

}
