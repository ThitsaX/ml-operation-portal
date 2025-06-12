package com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.HubUser;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.command.ModifyHubUser;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.repository.HubUserRepository;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.exception.HubUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyHubUserBean implements ModifyHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyHubUserBean.class);

    @Autowired
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
