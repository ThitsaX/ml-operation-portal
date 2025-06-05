package com.thitsaworks.dfsp_portal.iam.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.dfsp_portal.iam.domain.command.ModifyPrincipal;
import com.thitsaworks.dfsp_portal.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyPrincipalBean implements ModifyPrincipal {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyPrincipalBean.class);

    @Autowired
    private PrincipalRepository principalRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws PrincipalNotFoundException {

        Optional<Principal> optionalPrincipal = this.principalRepository.findById(input.getPrincipalId());

        if (!optionalPrincipal.isPresent()) {

            throw new PrincipalNotFoundException();

        }

        Principal principal = optionalPrincipal.get();

        this.principalRepository.save(principal.modify(input.getPrincipalStatus(),input.getUserRoleType()));

        return new Output(principal.getPrincipalId(), true);
    }

}
