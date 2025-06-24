package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.usecase.participant.RemoveExistingParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveExistingParticipantUserBean extends RemoveExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserBean.class);

    private final RemoveParticipantUser removeParticipantUser;

    private final ModifyPrincipal modifyPrincipal;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public RemoveExistingParticipantUser.Output onExecute(RemoveExistingParticipantUser.Input input) throws Exception {

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

        RemoveParticipantUser.Output output = this.removeParticipantUser.execute(
                new RemoveParticipantUser.Input(input.participantId(), input.participantUserId()));

        this.modifyPrincipal.execute(
                new ModifyPrincipal.Input(new PrincipalId(output.participantUserId().getId()),
                        PrincipalStatus.INACTIVE));

        return new RemoveExistingParticipantUser.Output(output.removed(), output.participantUserId());
    }

    @Override
    protected String getName() {

        return RemoveExistingParticipantUser.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_participant";
    }

    @Override
    protected String getId() {

        return RemoveExistingParticipantUser.class.getName();
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
            case SUPERUSER, REPORTING, OPERATION -> false;
        };

    }

    @Override
    public void onAudit(RemoveExistingParticipantUser.Input input, RemoveExistingParticipantUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, RemoveExistingParticipantUser.class, input, output,
                new UserId(securityContext.userId()),
                securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
