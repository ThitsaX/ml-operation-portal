package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.UnblockUserActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnblockUserActionCommandHandler implements UnblockUserActionCommand {

    private final UserRepository userRepository;

    private final IAMActionRepository IAMActionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<User> optUser = this.userRepository.findById(input.userId());

        if (optUser.isEmpty()) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);
        }

        Optional<IAMAction> optAction = this.IAMActionRepository.findById(input.actionId());

        if (optAction.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        User user = optUser.get();
        var unBlocked =  user.unblockAction(optAction.get());
        this.userRepository.saveAndFlush(user);

        return new Output(true, unBlocked);
    }

}
