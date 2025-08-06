package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUserProfile;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetUserProfileHandler extends OperationPortalUseCase<GetUserProfile.Input, GetUserProfile.Output>
    implements GetUserProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileHandler.class);

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    private final PrincipalCache principalCache;

    private final IAMQuery iamQuery;

    public GetUserProfileHandler(PrincipalCache principalCache,
                                 ActionAuthorizationManager actionAuthorizationManager,
                                 ParticipantCache participantCache,
                                 ParticipantUserCache participantUserCache, IAMQuery iamQuery) {

        super(principalCache, actionAuthorizationManager);

        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;
        this.principalCache = principalCache;
        this.iamQuery = iamQuery;
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

        var permittedMenuAndActionList =
                this.iamQuery.getMenusAndActionsByUserId(principalData.principalId());

        return new Output(participantUserData.participantUserId(),
                          participantUserData.name(),
                          participantUserData.email(),
                          participantUserData.firstName(),
                          participantUserData.lastName(),
                          participantUserData.jobTitle(),
                          participantUserData.participantId(),
                          participantUserData.createdDate(),
                          participantData.participantName()
                                         .getValue(),
                          participantData.description(),
                          null,
                          permittedMenuAndActionList);

    }

}
