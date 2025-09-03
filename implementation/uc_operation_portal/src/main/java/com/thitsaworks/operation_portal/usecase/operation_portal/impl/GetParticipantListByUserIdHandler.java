package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantListByUserId;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetParticipantListByUserIdHandler
    extends OperationPortalAuditableUseCase<GetParticipantListByUserId.Input, GetParticipantListByUserId.Output>
    implements GetParticipantListByUserId {

    private final ParticipantQuery participantQuery;

    private final PrincipalCache principalCache;

    private final PrincipalRoleQuery principalRoleQuery;

    private final RoleQuery roleQuery;

    public GetParticipantListByUserIdHandler(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache,
                                             ActionAuthorizationManager actionAuthorizationManager,
                                             ParticipantQuery participantQuery,
                                             PrincipalCache principalCache1,
                                             PrincipalRoleQuery principalRoleQuery,
                                             RoleQuery roleQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
        this.principalCache = principalCache1;
        this.principalRoleQuery = principalRoleQuery;
        this.roleQuery = roleQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        }

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        var principalRole = this.principalRoleQuery.getRole(principalData.principalId());

        var role = this.roleQuery.get(principalRole.roleId());
        if (role.isDfsp()) {

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