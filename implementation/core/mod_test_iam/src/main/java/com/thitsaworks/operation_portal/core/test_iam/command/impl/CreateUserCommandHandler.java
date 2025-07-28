package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateUserCommand;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CreateUserCommand {

    private final UserRepository userRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws IAMException {

        Optional<User> optionalPrincipal = this.userRepository.findOne(
            UserRepository.Filters.withUserId(input.userId())
                                  .and(UserRepository.Filters.withRealm(input.realmType())));

        if (optionalPrincipal.isPresent()) {

            throw new IAMException(IAMErrors.DUPLICATE_PRINCIPAL);
        }

        var user = new User(input.userId(),
                            input.realmType(),
                            input.passwordPlain(),
                            input.realmId(),
                            input.principalStatus());

        this.userRepository.save(user);

        return new Output(user.getAccessKey(), user.getSecretKey());
    }

}
