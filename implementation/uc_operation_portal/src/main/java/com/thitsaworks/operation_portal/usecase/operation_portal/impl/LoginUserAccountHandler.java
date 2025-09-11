package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.command.AuthenticateCommand;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.LoginUserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginUserAccountHandler implements LoginUserAccount {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountHandler.class);

    private final UserQuery userQuery;

    private final AuthenticateCommand authenticateCommand;

    public LoginUserAccountHandler(UserQuery userQuery,
                                   AuthenticateCommand authenticateCommand) {

        this.userQuery = userQuery;
        this.authenticateCommand = authenticateCommand;
    }

    @Override
    public Output execute(Input input) throws DomainException {

        UserData userData = this.userQuery.get(input.email());

        if (userData.userId() == null) {

            throw new ParticipantException(ParticipantErrors.EMAIL_NOT_FOUND.defaultMessage(
                    "System cannot find the provided [" + input.email() + "] email."));
        }

        AuthenticateCommand.Output securityToken = this.authenticateCommand.execute(
                new AuthenticateCommand.Input(new PrincipalId(userData.userId().getId()),
                        input.passwordPlain()));

        return new Output(securityToken.securityToken()
                                       .getAccessKey(),
                securityToken.securityToken()
                             .getSecretKey());
    }

}
