package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllOtherParticipants;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class GetAllOtherParticipantsBean
    extends OperationPortalUseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output>
    implements GetAllOtherParticipants {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ParticipantQuery participantQuery;

    public GetAllOtherParticipantsBean(PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       ParticipantQuery participantQuery) {

        super(PERMITTED_ROLES,
              principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<ParticipantData> participantDataList =
            this.participantQuery.getOtherParticipants(input.participantId());

        List<ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {
            participantInfoList.add(new ParticipantInfo(participantData.participantId(),
                                                        participantData.participantName(),
                                                        participantData.description()));
        }

        return new Output(participantInfoList);
    }

}
