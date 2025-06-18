package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import com.thitsaworks.operation_portal.core.participant.exception.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateHubUserHandler implements CreateHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHubUserHandler.class);

    private final HubUserRepository hubUserRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws EmailAlreadyRegisteredException {

        Optional<HubUser> optionalHubUserUser = this.hubUserRepository.findByEmail(input.email());

        if (optionalHubUserUser.isPresent()) {
            throw new EmailAlreadyRegisteredException(input.email().getValue());
        }

        HubUser hubUser = new HubUser(
                input.name(),
                input.email(),
                input.firstName(),
                input.lastName(),
                input.jobTitle());

        this.hubUserRepository.save(hubUser);

        return new CreateHubUser.Output(true, hubUser.getUserId());
    }

}
