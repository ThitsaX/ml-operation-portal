package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetParticipantListByParticipantHandler
    extends OperationPortalUseCase<GetParticipantListByParticipant.Input, GetParticipantListByParticipant.Output>
    implements GetParticipantListByParticipant {

    private final ParticipantQuery participantQuery;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    public GetParticipantListByParticipantHandler(PrincipalCache principalCache,
                                                  ActionAuthorizationManager actionAuthorizationManager,
                                                  ParticipantQuery participantQuery,
                                                  UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.format(securityContext.userId().toString()));
        }

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        if (this.userPermissionManager.isDfsp(principalData.principalId())) {

            var participantData = this.participantQuery.get(new ParticipantId(principalData.realmId()
                                                                                           .getId()));
            participantInfoList.add(
                new Output.ParticipantInfo(participantData.participantId(),
                                           participantData.participantName()
                                                          .getValue(),
                                           participantData.description()));

        } else {

            List<ParticipantData> participantDataList = this.participantQuery.getParticipants();

            for (ParticipantData participantData : participantDataList) {
                participantInfoList.add(
                    new Output.ParticipantInfo(participantData.participantId(),
                                               participantData.participantName()
                                                              .getValue(),
                                               participantData.description()));
            }

        }
        return new Output(participantInfoList);

    }

}