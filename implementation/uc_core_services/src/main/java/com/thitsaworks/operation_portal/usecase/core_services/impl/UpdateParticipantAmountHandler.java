package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.HubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.UpdateParticipantAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Set;

@Service
public class UpdateParticipantAmountHandler
        extends HubOperatorUseCase<UpdateParticipantAmount.Input, UpdateParticipantAmount.Output>
        implements UpdateParticipantAmount {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantAmountHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final HubClient hubClient;

    @Autowired
    public UpdateParticipantAmountHandler(PrincipalCache principalCache,
                                          HubClient hubClient) {

        super(PERMITTED_ROLES, principalCache);

        this.hubClient = hubClient;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        String participantId = "wallet1";

        String accountId = "6";

        PostParticipantBalance.Request request = new PostParticipantBalance.Request(input.transferId(),
                                                                                    input.externalReference(),
                                                                                    input.action(),
                                                                                    input.reason(),
                                                                                    input.amount());

        PostParticipantBalance.Response response = this.hubClient.postParticipantBalance(participantId,
                                                                                         accountId,
                                                                                         request);

        return new Output(response.accessKey(), response.secretKey());
    }

}
