package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.iam.domain.command.Authenticate;
import com.thitsaworks.operation_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.participant.exception.EmailNotFoundException;
import com.thitsaworks.operation_portal.participant.query.FindUserByEmail;
import com.thitsaworks.operation_portal.usecase.common.LoginUserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUserAccountBean extends LoginUserAccount {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountBean.class);

    @Autowired
    private FindUserByEmail findUserByEmail;

    @Autowired
    private Authenticate authenticate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @ReadTransactional
    public Output execute(Input input)
            throws PasswordAuthenticationFailureException, PrincipalNotFoundException,
            EmailNotFoundException {

        FindUserByEmail.Output findUserByEmailOutput =
                this.findUserByEmail.execute(new FindUserByEmail.Input(input.getEmail()));

        if (findUserByEmailOutput.getUserId().isEmpty()) {

            throw new EmailNotFoundException(input.getEmail().getValue());
        }

        Authenticate.Output securityToken = this.authenticate.execute(
                new Authenticate.Input(new PrincipalId(findUserByEmailOutput.getUserId().get().getId()),
                        input.getPasswordPlain()));

        return new Output(securityToken.getSecurityToken().getAccessKey(),
                securityToken.getSecurityToken().getSecretKey());
    }

    @Override
    protected Output onExecute(Input input) throws Exception {

        return null;
    }

    @Override
    protected String getName() {

        return LoginUserAccount.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_common";
    }

    @Override
    protected String getId() {

        return LoginUserAccount.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

    @Override
    public void onAudit(LoginUserAccount.Input input, LoginUserAccount.Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, LoginUserAccount.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
