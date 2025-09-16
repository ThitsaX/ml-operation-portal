package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
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

    private final UserCache userCache;

    private final PrincipalCache principalCache;

    private final IAMQuery iamQuery;

    public GetUserProfileHandler(PrincipalCache principalCache,
                                 ActionAuthorizationManager actionAuthorizationManager,
                                 ParticipantCache participantCache,
                                 UserCache userCache, IAMQuery iamQuery) {

        super(principalCache, actionAuthorizationManager);

        this.participantCache = participantCache;
        this.userCache = userCache;
        this.principalCache = principalCache;
        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        UserData userData = this.userCache.get(input.userId());

        PrincipalData principalData = this.principalCache.get(new PrincipalId(input.userId()
                                                                                   .getId()));

        if (userData == null || principalData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND.format(input.userId()
                                                                                        .getId()));
        }

        ParticipantData participantData = this.participantCache.get(userData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND.format(userData.participantId()
                                                                                                  .getId()));
        }

        var
            roleList =
            this.iamQuery.getRolesByPrincipal(principalData.principalId())
                         .stream()
                         .map(RoleData::name)
                         .toList();

        var permittedMenuAndActionList =
            this.iamQuery.getMenusAndActionsByUserId(principalData.principalId());

        return new Output(userData.userId(),
                          userData.name(),
                          userData.email(),
                          userData.firstName(),
                          userData.lastName(),
                          userData.jobTitle(),
                          userData.participantId(),
                          userData.createdDate(),
                          participantData.participantName()
                                         .getValue(),
                          participantData.description(),
                          participantData.logoDataType(),
                          participantData.logo(),
                          roleList,
                          permittedMenuAndActionList);

    }

}
