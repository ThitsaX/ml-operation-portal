package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantProfile;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GetParticipantProfileHandler
    extends OperationPortalUseCase<GetParticipantProfile.Input, GetParticipantProfile.Output>
    implements GetParticipantProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantProfileHandler.class);

    private final ParticipantQuery participantQuery;

    private final UserPermissionManager userPermissionManager;

    public GetParticipantProfileHandler(PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        ParticipantQuery participantQuery,
                                        UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        if (this.userPermissionManager.isDfsp(currentUser.principalId())) {
            if (!this.userPermissionManager.isSameParticipant(new ParticipantId(currentUser.realmId()
                                                                                           .getId()),
                                                              input.participantId())) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_USER_ACCESS);
            }
        }

        var participantData = this.participantQuery.get(input.participantId());

        return new Output(participantData.participantId(),
                          participantData.participantName()
                                         .getValue(),
                          participantData.description(),
                          participantData.address(),
                          participantData.mobile(),
                          participantData.logoFileType(),
                          participantData.logo(),
                          Instant.ofEpochSecond(participantData.createdDate()));
    }

    @Override
    protected void afterExecute(Output output) throws DomainException {

        Output modifiedOutput = new Output(output.participantId(),
                                           output.participantName(),
                                           output.description(),
                                           output.address(),
                                           output.mobile(),
                                           output.logoFileType(),
                                           null,
                                           output.createdDate());

        super.afterExecute(modifiedOutput);
    }

}
