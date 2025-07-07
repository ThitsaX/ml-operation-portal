package com.thitsaworks.operation_portal.usecase.common.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.command.Authenticate;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.common.LoginUserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class LoginUserAccountHandler implements LoginUserAccount {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ParticipantUserQuery participantUserQuery;

    private final Authenticate authenticate;

    public LoginUserAccountHandler(ParticipantUserQuery participantUserQuery,
                                   Authenticate authenticate) {

        this.participantUserQuery = participantUserQuery;
        this.authenticate = authenticate;
    }

    @Override
    public Output execute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserQuery.get(input.email());

        if (participantUserData.participantUserId() == null) {

            throw new ParticipantException(ParticipantErrors.EMAIL_NOT_FOUND);
        }

        Authenticate.Output securityToken = this.authenticate.execute(
            new Authenticate.Input(new PrincipalId(participantUserData.participantUserId()
                                                                      .getId()),
                                   input.passwordPlain()));

        return new Output(securityToken.securityToken()
                                       .getAccessKey(),
                          securityToken.securityToken()
                                       .getSecretKey());
    }

}
