package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.command.Authenticate;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.EmailNotFoundException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.common.LoginUserAccount;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserAccountHandler extends LoginUserAccount {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountHandler.class);

    private final ParticipantUserQuery participantUserQuery;

    private final Authenticate authenticate;

    private final ObjectMapper objectMapper;

    @Override
    public Output execute(Input input)
            throws PasswordAuthenticationFailureException, PrincipalNotFoundException,
                   EmailNotFoundException {

        ParticipantUserData participantUserData =
                this.participantUserQuery.get(input.email());

        if (participantUserData.participantUserId() == null) {

            throw new EmailNotFoundException(input.email().getValue());
        }

        Authenticate.Output securityToken = this.authenticate.execute(
                new Authenticate.Input(new PrincipalId(participantUserData.participantUserId().getId()),
                                       input.passwordPlain()));

        return new Output(securityToken.securityToken().getAccessKey(),
                          securityToken.securityToken().getSecretKey());
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
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
