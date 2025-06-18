package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUser;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyHubUserHandler implements ModifyHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyHubUserHandler.class);

    private HubUserRepository hubUserRepository;

    @Override
    @CoreWriteTransactional
    public ModifyHubUser.Output execute(ModifyHubUser.Input input) throws HubUserNotFoundException {

        Optional<HubUser> optionalHubUserUser = this.hubUserRepository.findById(input.hubUserId());

        if (optionalHubUserUser.isEmpty()) {
            throw new HubUserNotFoundException(input.hubUserId().getId().toString());
        }

        HubUser hubUser = optionalHubUserUser.get();

        this.hubUserRepository.save(
                hubUser.name(input.name())
                       .firstName(input.firstName())
                       .lastName(input.lastName())
                       .jobTitle(input.jobTitle())
        );

        return new Output(input.hubUserId(), true);
    }

}
