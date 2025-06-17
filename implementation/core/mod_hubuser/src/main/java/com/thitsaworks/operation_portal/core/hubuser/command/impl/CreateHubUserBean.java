package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
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
public class CreateHubUserBean implements CreateHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHubUserBean.class);

    private final HubUserRepository hubUserRepository;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws EmailAlreadyRegisteredException {

        Optional<HubUser> optionalHubUserUser = this.hubUserRepository.findByEmail(input.getEmail());

        if (optionalHubUserUser.isPresent()) {
            throw new EmailAlreadyRegisteredException(input.getEmail().getValue());
        }

        HubUser hubUser = new HubUser(
                input.getName(),
                input.getEmail(),
                input.getFirstName(),
                input.getLastName(),
                input.getJobTitle());

        this.hubUserRepository.save(hubUser);

        return new CreateHubUser.Output(true, hubUser.getUserId());
    }

}
