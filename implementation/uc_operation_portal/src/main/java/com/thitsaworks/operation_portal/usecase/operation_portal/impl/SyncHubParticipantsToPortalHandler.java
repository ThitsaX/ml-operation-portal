package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.SyncHubParticipantsToPortal;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final ObjectMapper objectMapper;

    public SyncHubParticipantsToPortalHandler(CreateInputAuditCommand createInputAuditCommand,
                                              CreateOutputAuditCommand createOutputAuditCommand,
                                              CreateExceptionAuditCommand createExceptionAuditCommand,
                                              ObjectMapper objectMapper,
                                              PrincipalCache principalCache,
                                              ActionAuthorizationManager actionAuthorizationManager,
                                              HubParticipantQuery hubParticipantQuery,
                                              ParticipantQuery participantQuery,
                                              CreateParticipantCommand createParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.hubParticipantQuery = hubParticipantQuery;
        this.participantQuery = participantQuery;
        this.createParticipantCommand = createParticipantCommand;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<HubParticipantData> hubParticipantDataList = this.hubParticipantQuery.getParticipantList();

        Set<String> existingParticipantNames = this.participantQuery.getParticipants().stream()
                                                                        .map(pd   -> pd.participantName().getValue())
                                                                        .collect(Collectors.toSet());

        List<CreatedParticipantInfo> createdParticipantInfoList = new ArrayList<>();

        for (HubParticipantData hubParticipant : hubParticipantDataList) {
            if (!existingParticipantNames.contains(hubParticipant.name())) {

                CreateParticipantCommand.Output output =
                        this.createParticipantCommand.execute(new CreateParticipantCommand.Input(new ParticipantName(
                                hubParticipant.name()),
                                                                                         hubParticipant.description(),
                                                                                         null,
                                                                                         null,

                                                                                         null,
                                                                                         null));

                createdParticipantInfoList.add(new CreatedParticipantInfo(output.participantId().getId().toString(),
                                                                          hubParticipant.name(),
                                                                          hubParticipant.description()));

            }
        }

        try {
            LOG.info("Created participants : {}",
                     this.objectMapper.writeValueAsString(createdParticipantInfoList));

        } catch (Exception e) {
            LOG.info("Something went wrong: {}", e.getMessage());
        }

        return new Output(true);
    }

    record CreatedParticipantInfo(String participantId, String name, String description) {}

}
