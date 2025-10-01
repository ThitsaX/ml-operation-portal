package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantStatusCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateParticipantStatus;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class UpdateParticipantStatusHandler
    extends OperationPortalAuditableUseCase<UpdateParticipantStatus.Input, UpdateParticipantStatus.Output>
    implements UpdateParticipantStatus {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantStatusHandler.class);

    private final ParticipantHubClient participantHubClient;

    private final ModifyParticipantStatusCommand modifyParticipantStatusCommand;

    @Autowired
    public UpdateParticipantStatusHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          ParticipantHubClient participantHubClient,
                                          ModifyParticipantStatusCommand modifyParticipantStatusCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantHubClient = participantHubClient;
        this.modifyParticipantStatusCommand = modifyParticipantStatusCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        boolean isActive = "active".equalsIgnoreCase(input.activeStatus());
        ParticipantStatus participantStatus = isActive ? ParticipantStatus.ACTIVE : ParticipantStatus.INACTIVE;

        PutParticipantStatus.Request request = new PutParticipantStatus.Request(input.participantName(),
                                                                                input.participantCurrencyId(),
                                                                                isActive);

        PutParticipantStatus.Response response = this.participantHubClient.putParticipantStatus(request);

         this.modifyParticipantStatusCommand.execute(new ModifyParticipantStatusCommand.Input(new ParticipantName(input.participantName()),
                                                                                                           participantStatus));


        GetParticipant.Response
            getParticipantResponse =
            this.participantHubClient.getParticipant(new GetParticipant.Request(input.participantName()));

        
        List<GetParticipant.Response.Account> accounts = getParticipantResponse.accounts();

        String status = accounts.stream()
                                .filter(account -> account.id() == input.participantCurrencyId())
                                .findFirst()
                                .map(account -> account.isActive() == 1 ? "active" : "inActive")
                                .orElse("Unknown");

        return new Output(request.participantName(), request.participantCurrencyId(), status);
    }

}
