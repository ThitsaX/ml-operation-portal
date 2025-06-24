package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUser;
import com.thitsaworks.operation_portal.usecase.participant.ModifyExistingParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyExistingParticipantUserBean extends ModifyExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUserBean.class);

    private final ModifyParticipantUser modifyParticipantUser;

    private final ModifyPrincipal modifyPrincipal;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        ModifyParticipantUser.Output output = this.modifyParticipantUser.execute(
                new ModifyParticipantUser.Input(input.participantUserId(), input.name(), input.email(),
                        input.firstName(), input.lastName(), input.jobTitle(), null));

        this.modifyPrincipal.execute(
                new ModifyPrincipal.Input(new PrincipalId(output.participantUserId().getId()), input.userRoleType(),
                        input.principalStatus()));

        return new Output(output.modified(), output.participantUserId());
    }

    @Override
    protected String getName() {

        return ModifyExistingParticipantUser.class.getCanonicalName();
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

        return ModifyExistingParticipantUser.class.getName();
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
            case OPERATION, SUPERUSER, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(ModifyExistingParticipantUser.Input input, ModifyExistingParticipantUser.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingParticipantUser.class, input, output,
                new UserId(securityContext.userId()),
                securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
