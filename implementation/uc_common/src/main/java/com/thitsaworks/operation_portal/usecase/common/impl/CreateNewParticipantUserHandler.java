package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUser;
import com.thitsaworks.operation_portal.usecase.common.CreateNewParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewParticipantUserHandler extends CreateNewParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserHandler.class);

    private final CreateParticipantUser createParticipantUser;

    private final CreatePrincipal createPrincipal;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(CreateNewParticipantUser.Input input) throws Exception {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId().getId().equals(input.participantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        CreateParticipantUser.Output output = this.createParticipantUser.execute(
                new CreateParticipantUser.Input(input.name(), input.email(), input.participantId(),
                                                input.firstName(), input.lastName(), input.jobTitle()));

        this.createPrincipal.execute(new CreatePrincipal.Input(new PrincipalId(output.participantUserId().getId()),
                                          input.realmType(),
                                          input.password(),
                                          new RealmId(input.participantId().getId()),
                                          input.userRoleType(),
                                          input.activeStatus()));

        return new Output(output.created());
    }

    @Override
    protected String getName() {

        return CreateNewParticipantUser.class.getCanonicalName();
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

        return CreateNewParticipantUser.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case ADMIN -> true;
            case SUPERUSER, OPERATION, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(CreateNewParticipantUser.Input input, CreateNewParticipantUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewParticipantUser.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
