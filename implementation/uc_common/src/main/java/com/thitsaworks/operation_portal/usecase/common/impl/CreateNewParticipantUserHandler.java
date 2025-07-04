package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUser;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.CreateNewParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewParticipantUserHandler
    extends CommonAuditableUseCase<CreateNewParticipantUser.Input, CreateNewParticipantUser.Output>
    implements CreateNewParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final CreateParticipantUser createParticipantUser;

    private final CreatePrincipal createPrincipal;

    private final PrincipalCache principalCache;

    public CreateNewParticipantUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           CreateParticipantUser createParticipantUser,
                                           CreatePrincipal createPrincipal) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createParticipantUser = createParticipantUser;
        this.createPrincipal = createPrincipal;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId()
                                  .getId()
                                  .equals(input.participantId()
                                               .getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        CreateParticipantUser.Output output = this.createParticipantUser.execute(
            new CreateParticipantUser.Input(input.name(), input.email(), input.participantId(),
                                            input.firstName(), input.lastName(), input.jobTitle()));

        this.createPrincipal.execute(new CreatePrincipal.Input(new PrincipalId(output.participantUserId()
                                                                                     .getId()),
                                                               input.realmType(),
                                                               input.password(),
                                                               new RealmId(input.participantId()
                                                                                .getId()),
                                                               input.userRoleType(),
                                                               input.activeStatus()));

        return new Output(output.created());
    }

}
