package com.thitsaworks.operation_portal.core.iam.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import com.thitsaworks.operation_portal.core.iam.domain.SecurityToken;
import com.thitsaworks.operation_portal.core.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.domain.command.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DfspWriteTransactional
public class AuthenticateBean implements Authenticate {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticateBean.class);

    @Autowired
    private PrincipalRepository principalRepository;

    @Override
    public Output execute(Input input) throws PasswordAuthenticationFailureException, PrincipalNotFoundException {

        Principal principal = this.principalRepository.findByPrincipalId(input.getPrincipalId())
                                                      .orElseThrow(PrincipalNotFoundException::new);

        SecurityToken securityToken = principal.authenticate(input.getPasswordPlain());

        this.principalRepository.save(principal);

        return new Authenticate.Output(securityToken);
    }

}
