package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserCache;
import com.thitsaworks.operation_portal.core.test_iam.command.ResetPasswordCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordCommandHandler implements ResetPasswordCommand {

    private  final UserRepository userRepository;

    private  final UserCache cache;
    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        var user =this.userRepository.findByUserId(input.userId())
                                     .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        AccessKey oldAccessKey = user.getAccessKey();

        user.reset(input.password());

        this.userRepository.save(user);
        this.cache.delete(oldAccessKey);

        return new Output(user.getAccessKey(),user.getSecretKey(),true);
    }
}
