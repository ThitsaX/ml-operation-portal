package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyHubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyHubUserBean implements ModifyHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyHubUserBean.class);

    private HubUserRepository hubUserRepository;

    @Override
    @DfspWriteTransactional
    public ModifyHubUser.Output execute(ModifyHubUser.Input input) throws HubUserNotFoundException {

        Optional<HubUser> optionalHubUserUser = this.hubUserRepository.findById(input.getHubUserId());

        if (optionalHubUserUser.isEmpty()) {
            throw new HubUserNotFoundException(input.getHubUserId().getId().toString());
        }

        HubUser hubUser = optionalHubUserUser.get();

        this.hubUserRepository.save(
                hubUser.name(input.getName())
                       .firstName(input.getFirstName())
                       .lastName(input.getLastName())
                       .jobTitle(input.getJobTitle())
        );

        return new Output(input.getHubUserId(), true);
    }

}
