package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCase;
import com.thitsaworks.operation_portal.usecase.participant.GetUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetUserProfileBean extends ParticipantUseCase<GetUserProfile.Input, GetUserProfile.Output>
    implements GetUserProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN, UserRoleType.OPERATION);

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    private final PrincipalCache principalCache;

    public GetUserProfileBean(PrincipalCache principalCache,
                              ParticipantCache participantCache,
                              ParticipantUserCache participantUserCache) {

        super(PERMITTED_ROLES, principalCache);

        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        PrincipalData principalData = this.principalCache.get(new PrincipalId(input.participantUserId()
                                                                                   .getId()));

        if (participantUserData == null || principalData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND);
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        return new Output(participantUserData.participantUserId(),
                          participantUserData.name(),
                          participantUserData.email(),
                          participantUserData.firstName(),
                          participantUserData.lastName(),
                          participantUserData.jobTitle(),
                          participantUserData.participantId(),
                          participantUserData.createdDate(),
                          participantData.dfspCode()
                                         .getValue(),
                          participantData.dfspName(),
                          principalData.userRoleType()
                                       .toString());

    }

}
