package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.SyncHubParticipantsToPortal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SyncHubParticipantsToPortalHandler
    extends OperationPortalAuditableUseCase<SyncHubParticipantsToPortal.Input, SyncHubParticipantsToPortal.Output>
    implements SyncHubParticipantsToPortal {

    private static final Logger LOG = LoggerFactory.getLogger(SyncHubParticipantsToPortalHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final HubParticipantQuery hubParticipantQuery;

    private final ParticipantQuery participantQuery;

    private final CreateParticipantCommand createParticipantCommand;

    public SyncHubParticipantsToPortalHandler(CreateInputAuditCommand createInputAuditCommand,
                                              CreateOutputAuditCommand createOutputAuditCommand,
                                              CreateExceptionAuditCommand createExceptionAuditCommand,
                                              ObjectMapper objectMapper,
                                              PrincipalCache principalCache,
                                              HubParticipantQuery hubParticipantQuery,
                                              ParticipantQuery participantQuery,
                                              CreateParticipantCommand createParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.hubParticipantQuery = hubParticipantQuery;
        this.participantQuery = participantQuery;
        this.createParticipantCommand = createParticipantCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<HubParticipantData> hubParticipantData = this.hubParticipantQuery.getParticipants();

        Set<String>
            existingParticipantNames =
            this.participantQuery.getParticipants()
                                 .stream()
                                 .map(ParticipantData::name)
                                 .collect(Collectors.toSet());

        for (HubParticipantData hubParticipant : hubParticipantData) {
            if (!existingParticipantNames.contains(hubParticipant.name())) {

                this.createParticipantCommand.execute(new CreateParticipantCommand.Input(hubParticipant.name(),
                                                                                         new DfspCode(hubParticipant.name()),
                                                                                         hubParticipant.name(),
                                                                                         null,
                                                                                         null,
                                                                                         "",
                                                                                         null,
                                                                                         null,
                                                                                         null));
            }
        }

        return new Output(true);
    }

}
