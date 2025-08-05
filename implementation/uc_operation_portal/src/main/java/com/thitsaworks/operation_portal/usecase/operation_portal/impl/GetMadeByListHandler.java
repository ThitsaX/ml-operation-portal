package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetMadeByList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.Set;

@Service
public class GetMadeByListHandler extends OperationPortalAuditableUseCase<GetMadeByList.Input, GetMadeByList.Output>
    implements GetMadeByList {

    private final ParticipantUserQuery participantUserQuery;

    public GetMadeByListHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                ParticipantUserQuery participantUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantUserQuery = participantUserQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var participantUsers = this.participantUserQuery.getParticipantUsers(input.participantId());

        Set<User> madeByUsers = new HashSet<>();

        for (var user : participantUsers) {

            madeByUsers.add(new User(new UserId(user.participantUserId()
                                                    .getEntityId()), user.name()));

        }

        return new Output(madeByUsers);
    }

}