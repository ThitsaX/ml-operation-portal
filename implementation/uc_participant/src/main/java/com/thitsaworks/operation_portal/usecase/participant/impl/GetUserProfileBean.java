package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.usecase.participant.GetUserProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileBean extends GetUserProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileBean.class);

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    private final PrincipalCache principalCache;

    private final ObjectMapper objectMapper;

    @Override
    public GetUserProfile.Output onExecute(GetUserProfile.Input input) throws Exception {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        PrincipalData principalData = this.principalCache.get(new PrincipalId(input.participantUserId().getId()));

        if (participantUserData == null || principalData == null) {

            throw new ParticipantUserNotFoundException(input.participantUserId().getId().toString());
        }

        ParticipantData participantData = participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.participantId().getId().toString());
        }

        return new Output(participantUserData.participantUserId(),
                          participantUserData.name(),
                          participantUserData.email(),
                          participantUserData.firstName(),
                          participantUserData.lastName(),
                          participantUserData.jobTitle(),
                          participantUserData.participantId(),
                          participantUserData.createdDate(),
                          participantData.dfspCode().getValue(),
                          participantData.dfspName(),
                          principalData.userRoleType().toString());

    }

    @Override
    protected String getName() {

        return GetUserProfile.class.getCanonicalName();
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

        return GetUserProfile.class.getName();
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
            case ADMIN, OPERATION -> true;
            case REPORTING, SUPERUSER -> false;
        };

    }

}
