package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUserCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyHubUserCommandHandler implements ModifyHubUserCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyHubUserCommandHandler.class);

    private HubUserRepository hubUserRepository;

    @Override
    @CoreWriteTransactional
    public ModifyHubUserCommand.Output execute(ModifyHubUserCommand.Input input) throws HubUserException {

        HubUser hubUser = this.hubUserRepository.findById(input.hubUserId()).orElseThrow(()->new HubUserException(HubUserErrors.USER_NOT_FOUND));

        this.hubUserRepository.save(
                hubUser.name(input.name())
                       .firstName(input.firstName())
                       .lastName(input.lastName())
                       .jobTitle(input.jobTitle())
        );

        return new Output(input.hubUserId(), true);
    }

}
