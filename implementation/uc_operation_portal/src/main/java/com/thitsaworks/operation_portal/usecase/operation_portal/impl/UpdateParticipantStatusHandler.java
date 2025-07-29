package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.HubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateParticipantStatus;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class UpdateParticipantStatusHandler
    extends OperationPortalAuditableUseCase<UpdateParticipantStatus.Input, UpdateParticipantStatus.Output>
    implements UpdateParticipantStatus {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantStatusHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final HubClient hubClient;

    @Autowired
    public UpdateParticipantStatusHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          HubClient hubClient,
                                          ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.hubClient = hubClient;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        boolean isActive = "active".equalsIgnoreCase(input.activeStatus());

        PutParticipantStatus.Request request = new PutParticipantStatus.Request(input.participantName(),
                                                                                input.participantCurrencyId(),
                                                                                isActive);

        PutParticipantStatus.Response response = this.hubClient.putParticipantStatus(request);

        GetParticipant.Response
            getParticipantResponse =
            this.hubClient.getParticipant(new GetParticipant.Request(input.participantName()));

        List<GetParticipant.Response.Account> accounts = getParticipantResponse.accounts();

        String status = accounts.stream()
                                .filter(account -> account.id() == input.participantCurrencyId())
                                .findFirst()
                                .map(account -> account.isActive() == 1 ? "active" : "inActive")
                                .orElse("Unknown");

        return new Output(request.participantName(), request.participantCurrencyId(), status);
    }

}
