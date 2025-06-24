package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.participant.GetExistingParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetExistingParticipantUserBean extends GetExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantUserBean.class);

    private final ParticipantUserQuery participantUserQuery;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        ParticipantUserData participantUserData = this.participantUserQuery.get(input.participantUserId());

        return new Output(participantUserData.participantUserId(),
                          participantUserData.name(),
                          participantUserData.email(),
                          participantUserData.firstName(),
                          participantUserData.lastName(),
                          participantUserData.jobTitle(),
                          participantUserData.participantId(),
                          participantUserData.createdDate(),
                          participantUserData.dfspCode().getValue());
    }

    @Override
    protected String getName() {

        return GetExistingParticipantUser.class.getCanonicalName();
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

        return GetExistingParticipantUser.class.getName();
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
    public void onAudit(Input input, Output output) {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetExistingParticipantUser.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
