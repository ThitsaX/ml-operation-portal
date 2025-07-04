package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.thitsaworks.operation_portal.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCase;
import com.thitsaworks.operation_portal.usecase.participant.GetAllOtherParticipants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllOtherParticipantsBean extends ParticipantUseCase<GetAllOtherParticipants.Input, GetAllOtherParticipants.Output> implements GetAllOtherParticipants {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final ParticipantQuery participantQuery;

    public GetAllOtherParticipantsBean(PrincipalCache principalCache, ParticipantQuery participantQuery) {

        super(PERMITTED_ROLES, principalCache);
        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        List<ParticipantData> participantDataList =
            this.participantQuery.getOtherParticipants(input.participantId());

        List<ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {
            participantInfoList.add(new ParticipantInfo(participantData.participantId(),
                                                        participantData.dfspCode(),
                                                        participantData.name()));
        }

        return new Output(participantInfoList);
    }

}
