package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUserCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateHubUserCommandHandler implements CreateHubUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHubUserCommandHandler.class);

    private final HubUserRepository hubUserRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws HubUserException {

        Optional<HubUser> optionalHubUserUser = this.hubUserRepository.findByEmail(input.email());

        if (optionalHubUserUser.isPresent()) {

            throw new HubUserException(HubUserErrors.EMAIL_ALREADY_REGISTERED);
        }

        HubUser hubUser = new HubUser(
                input.name(),
                input.email(),
                input.firstName(),
                input.lastName(),
                input.jobTitle());

        this.hubUserRepository.save(hubUser);

        return new CreateHubUserCommand.Output(true, hubUser.getUserId());
    }

}
