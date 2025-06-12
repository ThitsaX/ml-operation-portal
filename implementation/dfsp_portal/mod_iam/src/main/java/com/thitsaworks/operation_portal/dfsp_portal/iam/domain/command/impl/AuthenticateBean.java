package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.repository.PrincipalRepository;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.SecurityToken;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command.Authenticate;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.PrincipalStatus;
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

        Principal principal = this.principalRepository.findByPrincipalIdAndStatus(input.getPrincipalId(), PrincipalStatus.ACTIVE)
                .orElseThrow(PrincipalNotFoundException::new);

        SecurityToken securityToken = principal.authenticate(input.getPasswordPlain());

        this.principalRepository.save(principal);

        return new Authenticate.Output(securityToken);
    }

}
